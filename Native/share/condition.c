/*
 * Copyright (c) 2007 Sun Microsystems, Inc.  All rights reserved.
 *
 * Sun Microsystems, Inc. has intellectual property rights relating to technology embodied in the product
 * that is described in this document. In particular, and without limitation, these intellectual property
 * rights may include one or more of the U.S. patents listed at http://www.sun.com/patents and one or
 * more additional patents or pending patent applications in the U.S. and in other countries.
 *
 * U.S. Government Rights - Commercial software. Government users are subject to the Sun
 * Microsystems, Inc. standard license agreement and applicable provisions of the FAR and its
 * supplements.
 *
 * Use is subject to license terms. Sun, Sun Microsystems, the Sun logo, Java and Solaris are trademarks or
 * registered trademarks of Sun Microsystems, Inc. in the U.S. and other countries. All SPARC trademarks
 * are used under license and are trademarks or registered trademarks of SPARC International, Inc. in the
 * U.S. and other countries.
 *
 * UNIX is a registered trademark in the U.S. and other countries, exclusively licensed through X/Open
 * Company, Ltd.
 */
#include "log.h"

#include "condition.h"

void condition_initialize(Condition condition) {
#if log_MONITOR
  log_println("condition_initialize(" ADDRESS_FORMAT ", " ADDRESS_FORMAT ")", thread_self(), condition);
#endif
#if os_SOLARIS
    if (cond_init(condition, NULL, NULL) != 0) {
        c_ASSERT(false);
    }
#elif os_LINUX || os_DARWIN
    if (pthread_cond_init(condition, NULL) != 0) {
        c_ASSERT(false);
    }
#elif os_GUESTVMXEN
    *condition = guestvmXen_condition_create();
#endif

}

void condition_destroy(Condition condition) {
#if log_MONITOR
  log_println("condition_destroy   (" ADDRESS_FORMAT ", " ADDRESS_FORMAT ")", thread_self(), condition);
#endif
#if os_SOLARIS
    if (cond_destroy(condition) != 0) {
        c_ASSERT(false);
    }
#elif os_LINUX || os_DARWIN
    if (pthread_cond_destroy(condition) != 0) {
        c_ASSERT(false);
    }
#endif
}

int condition_wait(Condition condition, Mutex mutex) {
#if log_MONITOR
  log_println("condition_wait      (" ADDRESS_FORMAT ", " ADDRESS_FORMAT ", " ADDRESS_FORMAT ")", thread_self(), condition, mutex);
#endif
#if (os_DARWIN || os_LINUX)
    return pthread_cond_wait(condition, mutex);
#elif os_SOLARIS
    int ret = cond_wait(condition, mutex);
    if ( ret == EINTR) {
    	return 1;
    }
    return 0;
 #elif os_GUESTVMXEN
    return guestvmXen_condition_wait(*condition, *mutex, 0);
#endif
}

/*
 * This function returns true if the thread was interrupted, false otherwise
 */
Boolean condition_timedWait(Condition condition, Mutex mutex, Unsigned8 timeoutMilliSeconds) {
#if log_MONITOR
  log_println("condition_timedWait (" ADDRESS_FORMAT ", " ADDRESS_FORMAT ", " ADDRESS_FORMAT ", %d)", thread_self(), condition, mutex, timeoutMilliSeconds);
#endif
	int error = 0;
	if (timeoutMilliSeconds > 0) {

#       if (os_DARWIN || os_LINUX)
            struct timespec ts;
#           define TIMEDOUT ETIMEDOUT
#       elif os_SOLARIS
            timestruc_t ts;
#           define TIMEDOUT ETIME
#       elif os_GUESTVMXEN
            struct guestvmXen_TimeSpec ts;
#           define TIMEDOUT -1
#       endif

		ts.tv_sec = timeoutMilliSeconds / 1000;
		ts.tv_nsec = (timeoutMilliSeconds * 1000000) % 1000000000;

#       if (os_DARWIN || os_LINUX)
// TODO check error code for interrupted
            pthread_cond_timedwait(condition, mutex, &ts);
#       elif os_SOLARIS
            error = cond_reltimedwait(condition, mutex, &ts) == EINTR ? 1 : 0;
#       elif os_GUESTVMXEN
            error = guestvmXen_condition_wait(*condition, *mutex, &ts);
#       endif

	} else {
		error = condition_wait(condition, mutex);
	}
// At this point error should be 0 or 1 only!
	switch (error) {
		case 0: {
			return false;
		}
		case 1: {
			return true;
		}
		default: {
			log_println("condition_timedWait: unexpected error code %d", error);
			return false;
		}
	}


}

int condition_notify(Condition condition) {
#if log_MONITOR
  log_println("condition_notify    (" ADDRESS_FORMAT ", " ADDRESS_FORMAT ")", thread_self(), condition);
#endif
#if (os_DARWIN || os_LINUX)
    return pthread_cond_signal(condition);
#elif os_SOLARIS
    return cond_signal(condition);
#elif os_GUESTVMXEN
    return guestvmXen_condition_notify(*condition, 0);
#endif
}

int condition_notifyAll(Condition condition) {
#if log_MONITOR
  log_println("condition_notifyAll (" ADDRESS_FORMAT ", " ADDRESS_FORMAT ")", thread_self(), condition);
#endif
#if (os_DARWIN || os_LINUX)
	return pthread_cond_broadcast(condition);
#elif os_SOLARIS
	return cond_broadcast(condition);
#elif os_GUESTVMXEN
	return guestvmXen_condition_notify(*condition, 1);
#endif
}
