package com.fidelity.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fidelity.business.Gadget;
import com.fidelity.business.Product;
import com.fidelity.business.Widget;
import com.fidelity.integration.WarehouseDao;

/**
 * The business service that manipulates Widgets and Gadgets 
 * in the warehouse.
 * 
 * @author ROI Instructor Team
 */
@Service
@Transactional
public class WarehouseBusinessServiceImpl implements WarehouseBusinessService {
	@Autowired
	private WarehouseDao dao;

	// If you need a logger for debugging, uncomment the following two lines
	// @Autowired
	// private Logger logger;

	// ***** Widget Methods *****

	@Override
	public List<Widget> findAllWidgets() {
		List<Widget> widgets;
		
		try {
			widgets = dao.getAllWidgets();
		} 
		catch (Exception e) {
			String msg = "Error querying all Widgets in the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}
		
		return widgets;
	}

	@Override
	public Widget findWidgetById(int id) {
		validateId(id);

		Widget widget = null;
		try {
			widget = dao.getWidget(id);
		} 
		catch (Exception e) {
			String msg = String.format("Error querying For Widget with id = %d in the Warehouse database.", id);
			throw new WarehouseDatabaseException(msg, e);
		}
		
		return widget;
	}

	@Override
	public int removeWidget(int id) {
		validateId(id);

		int count = 0;
		try {
			count = dao.deleteWidget(id);
		} 
		catch (Exception e) {
			String msg = String.format("Error removing Widget with id = %d the Warehouse database.", id);
			throw new WarehouseDatabaseException(msg, e);
		}
		
		return count;
	}

	@Override
	public int addWidget(Widget widget) {
		validateWidget(widget);

		int count = 0;
		try {
			count = dao.insertWidget(widget);
		} 
		catch (DuplicateKeyException e) {
			throw e;  // re-throw exception
		}
		catch (Exception e) {
			String msg = "Error inserting Widget into the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}

		return count;
	}

	@Override
	public int modifyWidget(Widget widget) {
		validateWidget(widget);

		int count = 0;
		try {
			count = dao.updateWidget(widget);
		} 
		catch (Exception e) {
			String msg = "Error updating Widget in the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}

		return count;
	}

	private void validateId(int id) {
		if (id < 1) {
			throw new IllegalArgumentException("invalid widget id " + id);
		}
	}

	private void validateWidget(Widget widget) {
		validateProduct(widget);
		if (widget.getGears() < 1 ||
			widget.getSprockets() < 1 ) {
				throw new IllegalArgumentException("widget is not fully populated: " + widget);
		}
	}

	private void validateProduct(Product product) {
		if (product == null) {
			throw new IllegalArgumentException("product is null");
		}

		validateId(product.getId());

		if (product.getDescription() == null || product.getDescription().isBlank() ||
			product.getPrice() < 0) {
				throw new IllegalArgumentException("product is not fully populated: " + product);
		}
	}

	// ***** Gadget Methods *****

	@Override
	public List<Gadget> findAllGadgets() {
		List<Gadget> gadgets = null;
		
		try {
			gadgets = dao.getAllGadgets();
		} 
		catch (Exception e) {
			String msg = "Error querying for all Gadgets in the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}
		
		return gadgets;
	}

	@Override
	public Gadget findGadgetById(int id) {
		validateId(id);
		
		Gadget gadget = null;
		try {
			gadget = dao.getGadget(id);
		} 
		catch (Exception e) {
			String msg = String.format("Error querying for Gadget with id = %d in the Warehouse database.", id);
			throw new WarehouseDatabaseException(msg, e);
		}

		return gadget;
	}

	
	@Override
	public int removeGadget(int id) {
		validateId(id);
		
		int count = 0;
		try {
			count = dao.deleteGadget(id);
		} 
		catch (Exception e) {
			String msg = "Error removing Gadget in the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}
		
		return count;
	}

	@Override
	public int addGadget(Gadget gadget) {
		validateGadget(gadget);
		
		int count = 0;
		try {
			count = dao.insertGadget(gadget);
		} 
		catch (Exception e) {
			String msg = "Error inserting Gadget into the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}
		
		return count;
	}

	@Override
	public int modifyGadget(Gadget gadget) {
		validateGadget(gadget);
		
		int count = 0;
		try {
			count = dao.updateGadget(gadget);
		} 
		catch (Exception e) {
			String msg = "Error updating Gadget in the Warehouse database.";
			throw new WarehouseDatabaseException(msg, e);
		}

		return count;
	}

	private void validateGadget(Gadget gadget) {
		validateProduct(gadget);
		if (gadget.getCylinders() < 1 ) {
				throw new IllegalArgumentException("gadget is not fully populated: " + gadget);
		}
	}

	@Override
	public List<Product> findAllProducts() {
		List<Product> products = new ArrayList<>();
		try {
			List<Widget> widgets = dao.getAllWidgets();
			List<Gadget> gadgets = dao.getAllGadgets();

			products.addAll(widgets);
			products.addAll(gadgets);

			return products;
		} 
		catch (RuntimeException e) {
			throw new WarehouseDatabaseException(e);
		}
	}
	
}
