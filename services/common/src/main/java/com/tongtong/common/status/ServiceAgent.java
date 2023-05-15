package com.tongtong.common.status;

import com.tongtong.common.config.ServiceID;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ServiceAgent {
    private static String HOSTNAME = "UNKNOWN";


    static {
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
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
