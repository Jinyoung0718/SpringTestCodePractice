package com.sjy.dayontest.repository;

import com.sjy.dayontest.model.StudentPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPassRepository extends JpaRepository<StudentPass, Integer> {
}
