package com.fidelity.india.secondary.integration.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.fidelity.india.secondary.models.Sea;

@Mapper
public interface SeaMapper {
	Sea getPortName(String vessel);

}
