package com.fhy.filter;


import brave.Tracer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class Response1Filter extends ZuulFilter
{
	private static final int FILTER_ORDER =1;
	
	@Autowired
	Tracer tracer;
	
	@Override
	public String filterType()
	{
		return "post";
	}
	
	@Override
	public int filterOrder()
	{
		return FILTER_ORDER;
	}
	
	@Override
	public boolean shouldFilter()
	{
		return true;
	}
	
	@Override
	public Object run() throws ZuulException
	{
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.getResponse().addHeader("tmx-correlation-id2",tracer.currentSpan().context().traceIdString());
		return null;
	}
}
