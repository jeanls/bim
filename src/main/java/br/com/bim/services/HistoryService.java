package br.com.bim.services;

import br.com.bim.domains.History;
import br.com.bim.dtos.output.HistoryOutputDto;
import br.com.bim.repositories.HistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public List<HistoryOutputDto> getHistory(final String code) {
        final List<History> histories = historyRepository.findAllByGameCodeOrderByDateDesc(code);

        if (Objects.isNull(histories)) {
            return Collections.emptyList();
        }

        return histories.stream().map(history -> {
            final HistoryOutputDto historyOutputDto = new HistoryOutputDto();
            historyOutputDto.setMessage(history.getMessage());
            historyOutputDto.setTime(history.getDate());
            return historyOutputDto;
        }).collect(Collectors.toList());
    }
}
