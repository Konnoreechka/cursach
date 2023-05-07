package com.example.englishclass.Repository;

import com.example.englishclass.Entity.ProgramDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProgramDescriptionRepository extends JpaRepository<ProgramDescription, String> {
    @Query("SELECT u FROM ProgramDescription u WHERE u.program = :program")
    public ProgramDescription getProgramDescriptionByProgram(@Param("program") String program);
}
