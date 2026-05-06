package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.dto.request.AssignCollectivityIdentityRequest;
import org.hei_school.federation_agricole.dto.request.CreateCollectivityRequest;
import org.hei_school.federation_agricole.dto.request.CreateMembershipFee;
import org.hei_school.federation_agricole.entity.*;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.exception.ConflictException;
import org.hei_school.federation_agricole.exception.NotFoundException;
import org.hei_school.federation_agricole.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CollectivityService {

    private final TransactionRepository transactionRepository;
    private final CollectivityRepository repo;
    private final MemberRepository memberRepo;
    private final AccountRepository accountRepository;
    private final MembershipFeeRepository feeRepo;

    public CollectivityService(TransactionRepository transactionRepository,
                               CollectivityRepository repo,
                               MemberRepository memberRepo,
                               AccountRepository accountRepository,
                               MembershipFeeRepository feeRepo) {
        this.transactionRepository = transactionRepository;
        this.repo = repo;
        this.memberRepo = memberRepo;
        this.accountRepository = accountRepository;
        this.feeRepo = feeRepo;
    }

    private MemberEntity getMemberFull(String id) {
        MemberEntity m = memberRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found: " + id));
        m.setReferees(memberRepo.findRefereesByMemberId(id));
        return m;
    }

    public List<Collectivity> create(List<CreateCollectivityRequest> requests) {
        List<Collectivity> result = new ArrayList<>();
        for (CreateCollectivityRequest req : requests) result.add(createOne(req));
        return result;
    }

    private Collectivity createOne(CreateCollectivityRequest req) {
        if (req.getFederationApproval() == null || !req.getFederationApproval())
            throw new BadRequestException("Federation approval required");
        if (req.getMembers() == null || req.getMembers().size() < 10)
            throw new BadRequestException("Minimum 10 members required");
        if (req.getStructure() == null)
            throw new BadRequestException("Structure required");

        List<MemberEntity> members = new ArrayList<>();
        for (String id : req.getMembers()) members.add(getMemberFull(id));

        CollectivityStructure s = new CollectivityStructure();
        s.setPresident(getMemberFull(req.getStructure().getPresident()));
        s.setVicePresident(getMemberFull(req.getStructure().getVicePresident()));
        s.setTreasurer(getMemberFull(req.getStructure().getTreasurer()));
        s.setSecretary(getMemberFull(req.getStructure().getSecretary()));

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
        if (c.getName() != null || c.getNumber() != null)
            throw new ConflictException("Identity already assigned");
        if (repo.existsByName(req.getName()))
            throw new BadRequestException("Name already exists");
        if (repo.existsByNumber(req.getNumber()))
            throw new BadRequestException("Number already exists");

        repo.updateIdentity(id, req.getNumber(), req.getName());
        c.setName(req.getName());
        c.setNumber(req.getNumber());

        // Enrichir membres pour la réponse
        List<MemberEntity> fullMembers = new ArrayList<>();
        for (MemberEntity m : Optional.ofNullable(c.getMembers()).orElse(List.of()))
            fullMembers.add(getMemberFull(m.getId()));
        c.setMembers(fullMembers);
        return c;
    }

    public List<CollectivityTransaction> getTransactions(String id, LocalDate from, LocalDate to) {
        repo.findById(id).orElseThrow(() -> new NotFoundException("Collectivity not found"));
        if (from.isAfter(to)) throw new BadRequestException("Invalid date range");
        return transactionRepository.findByCollectivityAndPeriod(id, from, to);
    }

    public List<MembershipFee> getMembershipFees(String id) {
        repo.findById(id).orElseThrow(() -> new NotFoundException("Collectivity not found"));
        return feeRepo.findByCollectivity(id);
    }

    public List<MembershipFee> createMembershipFees(String id, List<CreateMembershipFee> requests) {
        repo.findById(id).orElseThrow(() -> new NotFoundException("Collectivity not found"));
        List<MembershipFee> result = new ArrayList<>();
        for (CreateMembershipFee req : requests) {
            if (req.getAmount() <= 0) throw new BadRequestException("Amount must be positive");
            if (req.getFrequency() == null) throw new BadRequestException("Frequency required");
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

    public List<FinancialAccount> getFinancialAccounts(String id, LocalDate atDate) {
        repo.findById(id).orElseThrow(() -> new NotFoundException("Collectivity not found"));
        return accountRepository.findByCollectivityAtDate(id, atDate);
    }

    public Collectivity getById(String id) {
        Collectivity c = repo.findByIdWithDetails(id);
        if (c == null) throw new NotFoundException("Collectivity not found");

        // Enrichir membres + référents
        List<MemberEntity> fullMembers = new ArrayList<>();
        for (MemberEntity m : Optional.ofNullable(c.getMembers()).orElse(List.of()))
            fullMembers.add(getMemberFull(m.getId()));
        c.setMembers(fullMembers);

        // Enrichir structure + référents
        CollectivityStructure s = c.getStructure();
        if (s != null) {
            s.setPresident(getMemberFull(s.getPresident().getId()));
            s.setVicePresident(getMemberFull(s.getVicePresident().getId()));
            s.setTreasurer(getMemberFull(s.getTreasurer().getId()));
            s.setSecretary(getMemberFull(s.getSecretary().getId()));
        }
        return c;
    }
}
