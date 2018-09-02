package com.netgloo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netgloo.models.MainAuditRegistration;
import com.netgloo.repo.MainRegistrationRepository;

@Service("AuditService")
public class AuditServiceImpl implements AuditService {

	@Autowired
	private MainRegistrationRepository mrepo;
	
	@Override
	public List<MainAuditRegistration> getAllAudits() {
		// TODO Auto-generated method stub
		return (List<MainAuditRegistration>) mrepo.findAll();
	}

	@Override
	public MainAuditRegistration getAuditById(long id) {
		// TODO Auto-generated method stub
		return mrepo.findOne(id);
	}

}
