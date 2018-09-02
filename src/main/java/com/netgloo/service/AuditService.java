package com.netgloo.service;

import java.util.List;

import com.netgloo.models.MainAuditRegistration;

public interface AuditService {

	public List<MainAuditRegistration> getAllAudits();
	public MainAuditRegistration getAuditById(long id);
	
}
