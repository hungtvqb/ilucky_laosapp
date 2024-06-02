/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Sungroup
 */
public class LuckyCodeUtils {

    public static AtomicInteger counterCdr = new AtomicInteger(0);

    public class Period {

        public static final int DAILY = 1;
        public static final int WEEKLY = 2;
        public static final int MONTHLY = 3;
    }

    public class Status {

        public static final int NOT_REDEEM = 0;
        public static final int REDEEM = 1;
        public static final int PROCESSED = 2;
    }

    public class Channel {

        public static final int MOCHA = 1;
        public static final int SMS = 2;
        public static final int USSD = 3;
        public static final int WEB = 4;
    }

    public class PrizeTimes {

        public static final int ALL_TIMES = 0;
        public static final int FIRST_TIMES = 1;
        public static final int FROM_SECOND_TIMES = 2;
    }

    public class NumberPrizeType {

        public static final int PERCENT = 0;
        public static final int FIXED_TIMES = 1;
    }
}
