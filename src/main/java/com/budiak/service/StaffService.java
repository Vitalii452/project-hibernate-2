package com.budiak.service;

import com.budiak.dao.StaffDAO;
import com.budiak.model.Staff;
import com.budiak.service.Exception.ServiceException;
import com.budiak.util.HibernateUtil;
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
        HibernateUtil.validateSession(session);

        LOGGER.debug("Attempting to find staff with id: {}", staffId);

        Staff staff = null;
        try {
            staff = staffDAO.findById(session, staffId);
        } catch (Exception e) {
            LOGGER.error("Error finding staff ", e);
            throw new ServiceException("Error finding staff", e);
        }

        if (staff == null) {
            LOGGER.info("No staff found with id: {}", staffId);
        } else {
            LOGGER.info("Staff found with id: {}", staffId);
        }
        return staff;
    }
}
