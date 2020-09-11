<%@ page contentType="text/html; charset=utf-8"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="res" uri="http://www.unidal.org/webres"%>
<jsp:useBean id="ctx" type="com.dianping.cat.report.page.state.Context" scope="request" />
<jsp:useBean id="payload" type="com.dianping.cat.report.page.state.Payload" scope="request" />
<jsp:useBean id="model" type="com.dianping.cat.report.page.state.Model" scope="request" />

<a:historyReport title="CAT State Report"
	navUrlPrefix="domain=${model.domain}&ip=${model.ipAddress}&show=${payload.show}">
	<jsp:attribute name="subtitle">${w:format(model.report.startTime,'yyyy-MM-dd HH:mm:ss')} to ${w:format(model.report.endTime,'yyyy-MM-dd HH:mm:ss')}</jsp:attribute>
	<jsp:body>
<table class="machines">
	<tr style="text-align: left">
		<th>&nbsp;[&nbsp; <c:choose>
				<c:when test="${model.ipAddress eq 'All'}">
					<a href="?op=history&show=${payload.show}&reportType=${payload.reportType}&domain=${model.domain}&date=${model.date}"
								class="current">All</a>
				</c:when>
				<c:otherwise>
					<a href="?op=history&show=${payload.show}&reportType=${payload.reportType}&domain=${model.domain}&date=${model.date}">All</a>
				</c:otherwise>
			</c:choose> &nbsp;]&nbsp; <c:forEach var="ip" items="${model.ips}">
   	  		&nbsp;[&nbsp;
   	  		<c:choose>
					<c:when test="${model.ipAddress eq ip}">
						<a href="?op=history&show=${payload.show}&reportType=${payload.reportType}&domain=${model.domain}&ip=${ip}&date=${model.date}"
									class="current">${ip}</a>
					</c:when>
					<c:otherwise>
						<a href="?op=history&show=${payload.show}&reportType=${payload.reportType}&domain=${model.domain}&ip=${ip}&date=${model.date}">${ip}</a>
					</c:otherwise>
				</c:choose>
   	 		&nbsp;]&nbsp;
			 </c:forEach>
		</th>
	</tr>
</table>
<table  class="table table-hover table-striped table-condensed" width="100%">
	<tr>
		<td width="10%"><a href="?op=history&domain=${model.domain}&ip=${model.ipAddress}&date=${model.date}&sort=domain&show=true">处理项目列表</a></td>
		<td width="10%" class="right"><a href="?op=history&domain=${model.domain}&ip=${model.ipAddress}&date=${model.date}&sort=total&show=true">处理消息总量</a></td>
		<td width="10%" class="right"><a href="?op=history&domain=${model.domain}&ip=${model.ipAddress}&date=${model.date}&sort=loss&show=true">丢失消息总量</a></td>
		<td width="10%" class="right"><a href="?op=history&domain=${model.domain}&ip=${model.ipAddress}&date=${model.date}&sort=size&show=true">压缩前消息大小(GB)</a></td>
		<td width="10%" class="right"><a href="?op=history&domain=${model.domain}&ip=${model.ipAddress}&date=${model.date}&sort=avg&show=true">平均消息大小(KB)</a></td>
		<td width="5%"  class="right"><a href="?op=history&domain=${model.domain}&ip=${model.ipAddress}&date=${model.date}&sort=machine&show=true">机器总数</a></td>
		<td width="45%">项目对应机器列表</td>
	</tr>
	<c:forEach var="item" items="${model.state.processDomains}"
			   varStatus="status">
		<tr class="">
			<c:set var="lastIndex" value="${status.index}" />
			<td>${item.name}</td>
			<td style="text-align: right;">${w:format(item.total,'#,###,###,###,##0.#')}</td>
			<td style="text-align: right;">${w:format(item.totalLoss,'#,###,###,###,##0.#')}</td>
			<td style="text-align: right;">${w:format(item.size/1024/1024/1024,'#,###,##0.000')}</td>
			<td style="text-align: right;">${w:format(item.avg/1024,'#,###,##0.000')}</td>
			<td style="text-align: center;">${w:size(item.ips)}</td>
			<td style="white-space: normal">${item.ips}</td>
		</tr>
		<tr class="graphs">
			<td colspan="10" style="display: none"><div id="${item.name}:total"
														style="display: none"></div></td>
		</tr>
		<tr class="graphs">
			<td colspan="10" style="display: none"><div id="${item.name}:totalLoss"
														style="display: none"></div></td>
		</tr>
		<tr class="graphs">
			<td colspan="10" style="display: none"><div id="${item.name}:size"
														style="display: none"></div></td>
		</tr>
		<tr></tr>
	</c:forEach>
	<tr style="color: white;">
		<td>${lastIndex+1}</td>
		<td>${model.state.totalSize}</td>
	</tr>
	<tr></tr>
	<tr class="graphs">
				<td colspan="4" style="display: none"><div id="delayAvg" style="display: none"></div></td>
			</tr>
</table>
</br>
<c:choose>
<c:when test="${payload.show == true}">
<%--<table  class="table table-hover table-striped table-condensed" width="100%">--%>

<%--</table>--%>
</c:when>
</c:choose>
<res:useJs value="${res.js.local['state_js']}" target="bottom-js" />
</jsp:body>
</a:historyReport>

<script type="text/javascript" src="/cat/js/appendHostname.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		appendHostname(${model.ipToHostnameStr});
		$("#warp_search_group").hide();
	});
</script>

