package com.fidelity.integration;

import java.util.List;

import com.fidelity.business.Gadget;
import com.fidelity.business.Widget;

public interface WarehouseDao {

	// Widget methods
	List<Widget> getAllWidgets();

	Widget getWidget(int id);

	int deleteWidget(int id);

	int insertWidget(Widget widget);

	int updateWidget(Widget widget);

	// Gadget methods
	List<Gadget> getAllGadgets();

	Gadget getGadget(int id);

	int deleteGadget(int id);

	int insertGadget(Gadget gadget);

	int updateGadget(Gadget gadget);

}
