package com.roifmr.presidents.integration.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.roifmr.presidents.models.President;

@Mapper
public interface PresidentsMapper {

	List<President> getAllPresidents();

	String getBiographyById(@Param("id") int id);
}
