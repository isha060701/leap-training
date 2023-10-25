package com.fidelity.integration.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fidelity.business.Gadget;
import com.fidelity.business.Widget;


@Mapper
public interface WarehouseMapper {
	// ***** Widget Methods *****
	List<Widget> getAllWidgets();
	Widget getWidget(@Param("id") int id);
	int deleteWidget(@Param("id") int id);
	int updateWidget(Widget widget);
	int insertWidget(Widget widget);

	// ***** Gadget Methods *****
	List<Gadget> getAllGadgets();
	Gadget getGadget(@Param("id") int id);
	int deleteGadget(@Param("id") int id);
	int updateGadget(Gadget gadget);
	int insertGadget(Gadget gadget);

 }
