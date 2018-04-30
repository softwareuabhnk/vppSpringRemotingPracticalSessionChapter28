package com.virtualpairprogrammers.restcontrollers;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.virtualpairprogrammers.domain.Action;
import com.virtualpairprogrammers.domain.Call;

@XmlRootElement
public class CallActionRepresentation {
	
	private Call call;
	private Collection<Action> actions;
	
	public CallActionRepresentation() {}

	public Call getCall() {
		return call;
	}

	public void setCall(Call call) {
		this.call = call;
	}

	public Collection<Action> getActions() {
		return actions;
	}

	public void setActions(Collection<Action> actions) {
		this.actions = actions;
	}
}
