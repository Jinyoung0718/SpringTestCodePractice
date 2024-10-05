package com.sjy.dayonetest.repository;

import com.sjy.dayonetest.model.StudentPass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPassRepository extends JpaRepository<StudentPass, Integer> {
}
