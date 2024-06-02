/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author kdvt_tungtt8
 * @version x.x
 * @since May 27, 2011
 */
public class ObjProcess {

    private Object process;
    private boolean running;
    //private int module;

//    public int getModule() {
//        return module;
//    }
//
//    public void setModule(int module) {
//        this.module = module;
//    }

    public Object getProcess() {
        return process;
    }

    public void setProcess(Object process) {
        this.process = process;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
