package org.hei_school.federation_agricole.service;

import org.hei_school.federation_agricole.config.DataSource;
import org.hei_school.federation_agricole.dto.MemberDTO;
import org.hei_school.federation_agricole.entity.MemberEntity;
import org.hei_school.federation_agricole.exception.BadRequestException;
import org.hei_school.federation_agricole.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Service
public class MemberService{
    private final MemberRepository repository;
    private final DataSource dataSource;

    public MemberService(MemberRepository repository, DataSource dataSource) { this.repository = repository;
        this.dataSource = dataSource;
    }

    public MemberEntity create(MemberDTO dto) throws Exception {

        MemberEntity m = new MemberEntity();
        m.setFirstName(dto.firstName);
        m.setLastName(dto.lastName);
        String generatedId = repository.save(m, dto.collectivityIdentifier);

        m.setId(generatedId);

        return m;
    }
    public Optional<MemberEntity> findById(String id) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT id FROM members WHERE id = ?")) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MemberEntity m = new MemberEntity();
                m.setId(rs.getString("id"));
                return Optional.of(m);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}