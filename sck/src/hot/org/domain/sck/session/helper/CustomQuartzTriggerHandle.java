package org.domain.sck.session.helper;

import java.io.Serializable;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.async.QuartzDispatcher;
import org.jboss.seam.async.QuartzTriggerHandle;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public class CustomQuartzTriggerHandle implements Serializable {

	private final String jobName;
	private final QuartzTriggerHandle triggerHandle;
	
	public CustomQuartzTriggerHandle(String jobName, QuartzTriggerHandle triggerHandle) {
		this.jobName = jobName;
		this.triggerHandle = triggerHandle;
	}

	public String getJobName() {
		return jobName;
	}

	public QuartzTriggerHandle getTriggerHandle() {
		return triggerHandle;
	}
	
	public StateTriggerHandle getCurrentStateOfTriggerHandle() {
		StateTriggerHandle state = null;
		try {
			Trigger trigger = triggerHandle.getTrigger();
			if (trigger == null)
				return StateTriggerHandle.CANCELED;
			
			QuartzDispatcher dispatcher = (QuartzDispatcher) Component.getInstance("org.jboss.seam.async.dispatcher", ScopeType.APPLICATION);
			if (dispatcher == null) return null;
			
			Scheduler scheduler = dispatcher.getScheduler();
			int triggerState = scheduler.getTriggerState(triggerHandle.getTrigger().getName(), triggerHandle.getTrigger().getGroup());
			switch (triggerState) {
				case Trigger.STATE_NORMAL:
					state = StateTriggerHandle.NORMAL;
					break;
				case Trigger.STATE_PAUSED:
					state = StateTriggerHandle.PAUSED;
					break;
				case Trigger.STATE_COMPLETE:
					state = StateTriggerHandle.COMPLETE;
					break;
				case Trigger.STATE_ERROR:
					state = StateTriggerHandle.ERROR;
					break;
				case Trigger.STATE_BLOCKED:
					state = StateTriggerHandle.BLOCKED;
					break;
				case Trigger.STATE_NONE:
					state = StateTriggerHandle.NONE;
					break;
				default:
					break;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return state;
	}
	
	public enum StateTriggerHandle {
		NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED, NONE, CANCELED;
	}

}