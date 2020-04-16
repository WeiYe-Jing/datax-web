package com.wugui.datatx.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * os utils
 */
public class OSUtils {

    private static final Logger logger = LoggerFactory.getLogger(OSUtils.class);

    private static final SystemInfo SI = new SystemInfo();
    public static final String TWO_DECIMAL = "0.00";

    private static HardwareAbstractionLayer hal = SI.getHardware();

    private OSUtils() {
    }


    /**
     * get memory usage
     * Keep 2 decimal
     *
     * @return percent %
     */
    public static double memoryUsage() {
        GlobalMemory memory = hal.getMemory();
        double memoryUsage = (memory.getTotal() - memory.getAvailable()) * 1.0 / memory.getTotal();
        DecimalFormat df = new DecimalFormat(TWO_DECIMAL);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(memoryUsage * 100));
    }


    /**
     * get available physical memory size
     * <p>
     * Keep 2 decimal
     *
     * @return available Physical Memory Size, unit: G
     */
    public static double availablePhysicalMemorySize() {
        GlobalMemory memory = hal.getMemory();
        double availablePhysicalMemorySize = (memory.getAvailable() + memory.getSwapUsed()) / 1024.0 / 1024 / 1024;

        DecimalFormat df = new DecimalFormat(TWO_DECIMAL);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(availablePhysicalMemorySize));

    }

    /**
     * get total physical memory size
     * <p>
     * Keep 2 decimal
     *
     * @return available Physical Memory Size, unit: G
     */
    public static double totalMemorySize() {
        GlobalMemory memory = hal.getMemory();
        double availablePhysicalMemorySize = memory.getTotal() / 1024.0 / 1024 / 1024;

        DecimalFormat df = new DecimalFormat(TWO_DECIMAL);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(availablePhysicalMemorySize));
    }


    /**
     * load average
     *
     * @return load average
     */
    public static double loadAverage() {
        double loadAverage = hal.getProcessor().getSystemLoadAverage();

        DecimalFormat df = new DecimalFormat(TWO_DECIMAL);

        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(loadAverage));
    }

    /**
     * get cpu usage
     *
     * @return cpu usage
     */
    public static double cpuUsage() {
        CentralProcessor processor = hal.getProcessor();
        double cpuUsage = processor.getSystemCpuLoad();

        DecimalFormat df = new DecimalFormat(TWO_DECIMAL);
        df.setRoundingMode(RoundingMode.HALF_UP);

        return Double.parseDouble(df.format(cpuUsage*100));
    }


    /**
     * check memory and cpu usage
     *
     * @return check memory and cpu usage
     */
    public static Boolean checkResource(double systemCpuLoad, double systemReservedMemory) {
        // judging usage
        double loadAverage = OSUtils.loadAverage();
        //
        double availablePhysicalMemorySize = OSUtils.availablePhysicalMemorySize();

        if (loadAverage > systemCpuLoad || availablePhysicalMemorySize < systemReservedMemory) {
            logger.warn("load or availablePhysicalMemorySize(G) is too high, it's availablePhysicalMemorySize(G):{},loadAvg:{}", availablePhysicalMemorySize, loadAverage);
            return false;
        } else {
            return true;
        }
    }

}
