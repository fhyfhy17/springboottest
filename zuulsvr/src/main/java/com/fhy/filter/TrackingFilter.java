package com.fhy.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class TrackingFilter extends ZuulFilter
{
	@Autowired
	private FilterUtils filterUtils;
	
	@Override
	public String filterType()
	{
		return FilterConstants.PRE_TYPE;
	}
	
	@Override
	public int filterOrder()
	{
		return FilterConstants.PRE_DECORATION_FILTER_ORDER;
	}
	
	@Override
	public boolean shouldFilter()
	{
		return true;
	}
	
	private boolean isCorrelationIdPresent()
	{
		if(filterUtils.getCorrelationId()!=null)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public Object run() throws ZuulException
	{
		if(isCorrelationIdPresent())
		{
			log.info("zuul 查看 有关联id ={}",filterUtils.getCorrelationId());
		}
		else
		{
			filterUtils.setCorrelationId(UUID.randomUUID().toString());
			log.info("zuul 查看 没有关联id ,设置id ={}",filterUtils.getCorrelationId());
		}
		RequestContext ctx=RequestContext.getCurrentContext();
		log.info("zuul请求， {}",ctx.getRequest().getRequestURI());
		return null;
	}
	
}
