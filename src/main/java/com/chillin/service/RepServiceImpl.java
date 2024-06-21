package com.chillin.service;

import com.chillin.domain.Rep;
import com.chillin.dto.RepDTO;
import com.chillin.repository.rep.RepRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepServiceImpl implements RepService{

    private final RepRepository repRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<RepDTO> getReps(Long bid) {
        List<RepDTO> list = repRepository.getReps(bid);

        return list;
    }
}
