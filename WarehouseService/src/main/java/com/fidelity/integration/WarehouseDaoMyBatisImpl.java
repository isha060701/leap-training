package com.fidelity.integration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fidelity.business.Gadget;
import com.fidelity.business.Widget;
import com.fidelity.integration.mapper.WarehouseMapper;

@Repository("warehouseDao")
public class WarehouseDaoMyBatisImpl implements WarehouseDao {
	@Autowired
	private WarehouseMapper mapper;
		
	@Override
	public List<Widget> getAllWidgets() {
		List<Widget> products = mapper.getAllWidgets();
		return products;
	}
	
	@Override
	public Widget getWidget(int id) {
		Widget widget = mapper.getWidget(id);
		return widget;
	}

	@Override
	public int deleteWidget(int id) {
		int count = mapper.deleteWidget(id);
		return count;
	}

	@Override
	public int insertWidget(Widget widget) {
		int count = mapper.insertWidget(widget);
		return count;
	}

	@Override
	public int updateWidget(Widget widget) {
		int count = mapper.updateWidget(widget);
		return count;
	}

	// Gadget methods
	@Override
	public List<Gadget> getAllGadgets() {
		List<Gadget> products = mapper.getAllGadgets();
		return products;
	}

	@Override
	public Gadget getGadget(int id) {
		Gadget gadget = mapper.getGadget(id);
		return gadget;
	}
	
	@Override
	public int deleteGadget(int id) {
		int count = mapper.deleteGadget(id);
		return count;
	}

	@Override
	public int insertGadget(Gadget gadget) {
		int count = mapper.insertGadget(gadget);
		return count;
	}

	@Override
	public int updateGadget(Gadget gadget) {
		int count = mapper.updateGadget(gadget);
		return count;
	}

}
