package br.com.bim.repositories;

import br.com.bim.domains.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    List<History> findAllByGameCodeOrderByDateDesc(String code);
}
