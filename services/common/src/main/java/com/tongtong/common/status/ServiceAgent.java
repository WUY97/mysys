package com.tongtong.common.status;

import com.tongtong.common.config.ServiceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ServiceAgent {

    private static String HOSTNAME = "UNKNOWN";
    private static final Logger logger = LoggerFactory.getLogger(ServiceAgent.class);

    static {
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("Unable to get Hostname");
            logger.error(e.getMessage(), e);
        }
    }

    public static ServiceStatus getServiceStatus(ServiceID serviceID) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        ServiceStatus status = new ServiceStatus(serviceID.toString());
        status.setServiceHost(HOSTNAME);
        status.setServiceTime(dateFormat.format(cal.getTime()));
        return status;
    }

}
