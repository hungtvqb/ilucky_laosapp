/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.sql.Timestamp;

/**
 *
 * @author sungroup
 */
public class ProcessedDate implements Record {

    private Timestamp lastProcess;
    private int typeProcess;
    private long lastValue;

    public long getLastValue() {
        return lastValue;
    }

    public void setLastValue(long lastValue) {
        this.lastValue = lastValue;
    }

    public Timestamp getLastProcess() {
        return lastProcess;
    }

    public void setLastProcess(Timestamp lastProcess) {
        this.lastProcess = lastProcess;
    }

    public int getTypeProcess() {
        return typeProcess;
    }

    public void setTypeProcess(int typeProcess) {
        this.typeProcess = typeProcess;
    }

    @Override
    public long getID() {
        return lastProcess.getTime();
    }
}
