package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.dto.request.AssignCollectivityIdentityRequest;
import org.hei_school.federation_agricole.dto.request.CreateCollectivityRequest;
import org.hei_school.federation_agricole.dto.request.CreateMembershipFee;
import org.hei_school.federation_agricole.entity.*;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.exception.ConflictException;
import org.hei_school.federation_agricole.exception.NotFoundException;
import org.hei_school.federation_agricole.repository.CollectivityRepository;
import org.hei_school.federation_agricole.repository.MemberRepository;
import org.hei_school.federation_agricole.repository.MembershipFeeRepository;
import org.hei_school.federation_agricole.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CollectivityService {

    private final TransactionRepository transactionRepository;
    private final CollectivityRepository repo;
    private final MemberRepository memberRepo;
    private final MembershipFeeRepository feeRepo;

    public CollectivityService(TransactionRepository transactionRepository, CollectivityRepository repo, MemberRepository memberRepo,  MembershipFeeRepository feeRepo) {
        this.transactionRepository = transactionRepository;
        this.repo = repo;
        this.memberRepo = memberRepo;
        this.feeRepo = feeRepo;
    }

    private MemberEntity getMember(String id) {
        return memberRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found: " + id));
    }

    public List<Collectivity> create(List<CreateCollectivityRequest> requests) {
        List<Collectivity> result = new ArrayList<>();

        for (CreateCollectivityRequest req : requests) {
            result.add(createOne(req));
        }

        return result;
    }

    private Collectivity createOne(CreateCollectivityRequest req) {

        //validation métier

        if (req.getFederationApproval() == null || !req.getFederationApproval()) {
            throw new BadRequestException("Federation approval required");
        }

        if (req.getMembers() == null || req.getMembers().size() < 10) {
            throw new BadRequestException("Minimum 10 members required");
        }

        if (req.getStructure() == null) {
            throw new BadRequestException("Structure required");
        }

        // récupérer membres
        List<MemberEntity> members = new ArrayList<>();
        for (String id : req.getMembers()) {
            members.add(
                    memberRepo.findById(id)
                            .orElseThrow(() -> new NotFoundException("Member not found: " + id))
            );
        }

        // structure
        CollectivityStructure s = new CollectivityStructure();
        s.setPresident(getMember(req.getStructure().getPresident()));
        s.setVicePresident(getMember(req.getStructure().getVicePresident()));
        s.setTreasurer(getMember(req.getStructure().getTreasurer()));
        s.setSecretary(getMember(req.getStructure().getSecretary()));

        // validation structure
        Set<String> ids = new HashSet<>();
        for (MemberEntity m : members) ids.add(m.getId());

        if (!ids.contains(s.getPresident().getId()) ||
                !ids.contains(s.getVicePresident().getId()) ||
                !ids.contains(s.getTreasurer().getId()) ||
                !ids.contains(s.getSecretary().getId())) {

            throw new BadRequestException("Structure members must belong to collectivity");
        }

        // création
        Collectivity c = new Collectivity();
        c.setId(UUID.randomUUID().toString());
        c.setLocation(req.getLocation());
        c.setMembers(members);
        c.setStructure(s);

        repo.save(c);

        return c;
    }

    public Collectivity assignIdentity(String id, AssignCollectivityIdentityRequest req) {

        Collectivity c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Collectivity not found"));


        if (c.getName() != null || c.getNumber() != null) {
            throw new ConflictException("Identity already assigned");
        }
        if (repo.existsByName(req.getName())) {
            throw new BadRequestException("Name already exists");
        }
        if (repo.existsByNumber(req.getNumber())) {
            throw new BadRequestException("Number already exists");
        }
        repo.updateIdentity(id, req.getNumber(), req.getName());

        c.setName(req.getName());
        c.setNumber(req.getNumber());

        return c;
    }

    public List<CollectivityTransaction> getTransactions(
            String id,
            LocalDate from,
            LocalDate to
    ) {

        // vérifier collectivité existe
        repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Collectivity not found"));

        if (from.isAfter(to)) {
            throw new BadRequestException("Invalid date range");
        }

        return transactionRepository.findByCollectivityAndPeriod(id, from, to);
    }

    public List<MembershipFee> getMembershipFees(String id) {

        repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Collectivity not found"));

        return feeRepo.findByCollectivity(id);
    }

    public List<MembershipFee> createMembershipFees(
            String id,
            List<CreateMembershipFee> requests
    ) {

        repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Collectivity not found"));

        List<MembershipFee> result = new ArrayList<>();

        for (CreateMembershipFee req : requests) {

            if (req.getAmount() <= 0) {
                throw new BadRequestException("Amount must be positive");
            }

            if (req.getFrequency() == null) {
                throw new BadRequestException("Frequency required");
            }

            MembershipFee f = new MembershipFee();
            f.setId(UUID.randomUUID().toString());
            f.setCollectivityId(id);
            f.setEligibleFrom(req.getEligibleFrom());
            f.setFrequency(req.getFrequency());
            f.setAmount(req.getAmount());
            f.setLabel(req.getLabel());
            f.setStatus("ACTIVE");

            result.add(f);
        }

        feeRepo.saveAll(result);

        return result;
    }

    public Collectivity getById(String id) {

        Collectivity c = repo.findByIdWithDetails(id);

        if (c == null) {
            throw new NotFoundException("Collectivity not found");
        }


        List<MemberEntity> fullMembers = new ArrayList<>();

        for (MemberEntity m : c.getMembers()) {
            fullMembers.add(getMember(m.getId()));
        }

        c.setMembers(fullMembers);

        CollectivityStructure s = c.getStructure();

        if (s != null) {
            s.setPresident(getMember(s.getPresident().getId()));
            s.setVicePresident(getMember(s.getVicePresident().getId()));
            s.setTreasurer(getMember(s.getTreasurer().getId()));
            s.setSecretary(getMember(s.getSecretary().getId()));
        }

        return c;
    }
}
