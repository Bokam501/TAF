package com.hcl.atf.taf.dao;

import java.util.List;

import com.hcl.atf.taf.model.Processor;

public interface ProcessorDAO  {
	List<Processor> list();
	void add(Processor processor);
	Processor getProcessorByProcessorName(String processorName);
	Processor getProcessorByProcessorId(Integer processorId);	
}
