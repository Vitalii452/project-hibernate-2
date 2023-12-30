package com.budiak.service;

import com.budiak.dao.StaffDAO;
import com.budiak.model.Staff;
import com.budiak.service.Exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class StaffService {

    private static final Logger LOGGER = LogManager.getLogger(StaffService.class);
    private final StaffDAO staffDAO;

    public StaffService(StaffDAO staffDAO) {
        this.staffDAO = staffDAO;
    }

    public Staff findStaffById(Session session, byte staffId) {
        if (session == null || !session.isOpen()) {
            LOGGER.error("Session is closed or null");
            throw new ServiceException("Session is not open");
        }

        LOGGER.debug("Attempting to find staff with id: {}", staffId);
        Staff staff = staffDAO.findById(session, staffId);
        if (staff == null) {
            LOGGER.info("No staff found with id: {}", staffId);
        } else {
            LOGGER.info("staff found with id: {}", staffId);
        }
        return staff;
    }
}
