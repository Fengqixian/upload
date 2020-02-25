package com.clinbrain.bd.mdm.MetadataManage.technologyView.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.Database;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlDetail;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlSource;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.EtlTarget;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.entity.vo.*;
import com.clinbrain.bd.mdm.MetadataManage.technologyView.mapper.*;
import com.clinbrain.bd.mdm.common.core.util.R;
import com.clinbrain.bd.mdm.common.core.util.WebUtils;
import com.mzlion.easyokhttp.HttpClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


/**
 * @author LIANGLELE
 * @className com.clinbrain.bd.mdm.MetadataManage.service.impl.TechnologyViewServiceImpl
 * @createdDate 2019/10/13 11:26
 * @description TODO
 * @e-mail lianglele@clinbrain.com
 * @group bigdata develop group (mdm)
 */
@Service
public class TechnologyEtlServiceImpl implements TechnologyEtlService {
    @Value("${Etl.httpclient.data_url}")
    private String url;

    @Value("${Etl.data.jobId}")
    private Integer jobId;

    @Value("${Etl.data.hostCode}")
    private String hostCode;

    @Autowired
    private TechnologyEtlSourceMapper technologyEtlSourceMapper;
    @Autowired
    private TechnologyEtlTargetMapper technologyEtlTargetMapper;
    @Autowired
    private TechnologyEtlDetailMapper technologyEtlDetailMapper;
    @Autowired
    private TechnologyDatabaseMapper technologyDatabaseMapper;


    /**
     * 根据来源id和表id查询ETL详情
     *
     * @param sourceId 来源标识
     * @param targetId 目标标识
     * @return
     */
    @Override
    public EtlInfoVo selectEtlInfo(Integer sourceId, Integer targetId) {

        EtlInfoVo infoVo = new EtlInfoVo();
//        infoVo.setModuleCode("ETL_DIM_test_type_001_" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        EtlSource source = technologyEtlSourceMapper.selectById(sourceId);

        EtlTarget target = technologyEtlTargetMapper.selectById(targetId);
        infoVo.setModuleName(source.getSourceDb() + "到" + target.getTargetDb() + "_" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        //获取详情
        List<EtlDetail> detailList = technologyEtlDetailMapper.selectList(Wrappers.<EtlDetail>query().lambda().eq(EtlDetail::getSourceDbId, sourceId).eq(EtlDetail::getTargetDbId, targetId));
        //获取对于数据库信息
        Database sourceDb = technologyDatabaseMapper.selectById(source.getSourceDbId());
        Database targetDb = technologyDatabaseMapper.selectById(target.getTargetDbId());

        infoVo.setConnectionCode(targetDb.getDatabaseType());
        infoVo.setTargetSchema(targetDb.getNameEn());
        infoVo.setTargetTable(target.getTargetTable());
        infoVo.setModulePriority(1);
        infoVo.setModuleCategory("DIM");
        infoVo.setEtlType(1);
        infoVo.setEnabled(1);
        infoVo.setFullWhileMonths(1);
        infoVo.setJobId(jobId);
        infoVo.setHospitalName(hostCode);
        infoVo.setRangeEndDate(0L);
        infoVo.setRangeStartDate(0L);
        infoVo.setCreatedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        infoVo.setUpdatedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        List<Object> workflows = new ArrayList<>();
        EtlWorkflows workflow = new EtlWorkflows();
        workflow.setLoc("33.140625 -63");
        workflow.setRunnable(1);
        workflow.setColor("#00A9C9");
        workflow.setGroup(true);
        workflow.setTargetSchema(targetDb.getNameEn());
        workflow.setTargetTable(target.getTargetTable());


        EtlExtraData extraData = new EtlExtraData();

        EtlConnection scon = new EtlConnection();
        scon.setConnectionCode(sourceDb.getDatabaseType());

        scon.setUrl(this.getDataBaseUrl(sourceDb));
        scon.setEngineIdw(6);
        scon.setDataflowReader("hive".equalsIgnoreCase(sourceDb.getDatabaseType()) ? "hbase11x" : sourceDb.getDatabaseType() + "reader");
        scon.setDataflowWriter(sourceDb.getDatabaseType() + "writer");
        extraData.setConnection(scon);
        scon = new EtlConnection();
        scon.setConnectionCode(sourceDb.getDatabaseType());

        scon.setUrl(this.getDataBaseUrl(targetDb));
        scon.setEngineIdw(1);
        scon.setDataflowReader("hive".equalsIgnoreCase(targetDb.getDatabaseType()) ? "hbase11x" : targetDb.getDatabaseType() + "reader");
        scon.setDataflowWriter(targetDb.getDatabaseType() + "writer");
        EtlSql sql = new EtlSql();
        sql.setWhere("1=1");
        sql.setSelect(new ArrayList<>());
        EtlFrom from = new EtlFrom();
        sql.setFrom(from);
        extraData.setSql(sql);
        extraData.setTargetConnection(scon);
        extraData.setFullSql(target.getExecutorSql());
        workflow.setExtraData(extraData);


        workflow.setKey("DataXComponent");
        workflow.setText("配置输入输出");
        workflow.setCode("DataXComponent_1");
        workflow.setSequenceCustomized(1);
        workflow.setParamDefine("[{\"column\":\"speed.channel\",\"desc\":\"通道数\",\"default\":\"1\"}]");
        workflow.setComponentCategory("datax");
        workflow.setCategory("Group");
        workflow.setDesc("all");
        workflows.add(workflow);

        EtlLoc loc = new EtlLoc();
        loc.setColor("lightgreen");
        loc.setGroup("DataXComponent");

        loc.setKey("DataXComponent_mysqlreader");
        loc.setText("mysql库读取");
        loc.setCode("DataXComponent_1");
        loc.setSequenceCustomized(1);
        loc.setParamDefine("[{\"column\":\"speed.channel\",\"desc\":\"通道数\",\"default\":\"1\"}]");
        loc.setComponentCategory("datax");
        loc.setCategory("GroupNode");
        loc.setFlowType(1);
        workflows.add(loc);

        loc = new EtlLoc();
        loc.setColor("pink");
        loc.setGroup("DataXComponent");
        loc.setKey("DataXComponent_hivewriter");
        loc.setText("hivejdbc写入");
        loc.setCode("DataXComponent_1");
        loc.setSequenceCustomized(1);
        loc.setParamDefine("[{\"column\":\"speed.channel\",\"desc\":\"通道数\",\"default\":\"1\"}]");
        loc.setComponentCategory("datax");
        loc.setCategory("GroupNode");
        loc.setFlowType(2);
        workflows.add(loc);

        infoVo.setWorkflows(workflows);

        //调用etl创建
        this.startEtlJob(infoVo);
        return infoVo;
    }


    public void startEtlJob(EtlInfoVo infoVo) {
        String responseData = HttpClient.textBody(url).header("Authorization", "Basic MTox").json(infoVo)
                //设置编码
                .charset("utf-8")
                .asString();
        System.out.printf(responseData);
    }


    /**
     * 组装数据库连接
     *
     * @param db
     * @return
     */
    private String getDataBaseUrl(Database db) {

        String showIp = "jdbc:mysql";
        if ("hive".equalsIgnoreCase(db.getDatabaseType())) {
            showIp = "jdbc:hive2:";
        } else if ("gp".equalsIgnoreCase(db.getDatabaseType())) {
            showIp = "jdbc:pivotal:greenplum:";
        } else if ("sqlserver".equalsIgnoreCase(db.getDatabaseType())) {
            showIp = "jdbc:sqlserver:";
        } else if ("sqlserver".equalsIgnoreCase(db.getDatabaseType())) {
            showIp = "jdbc:oracle:thin:@";
        }

        showIp += "//" + db.getConnectIp() + ":" + db.getConnectHost();
        return showIp;
    }

    /**
     * 创建库
     *
     * @param source 来源
     * @param target 目标
     * @return
     */
    @Transactional
    @Override
    public R saveEtlDatabase(EtlSource source, EtlTarget target) {
        try {
            technologyEtlTargetMapper.insert(target);
            source.setEtlId(target.getId());
            technologyEtlSourceMapper.insert(source);

            return new R();
        } catch (Exception e) {
            return new R().setCode(1).setMsg("创建失败");
        }

    }

    /**
     * 修改
     *
     * @param source 来源
     * @param target 目标
     * @return
     */
    @Transactional
    @Override
    public R updateEtlDatabase(EtlSource source, EtlTarget target) {
        try {
            technologyEtlSourceMapper.updateById(source);
            technologyEtlTargetMapper.updateById(target);
            return new R();
        } catch (Exception e) {
            return new R().setCode(1).setMsg("修改失败");
        }

    }


    /**
     * 查询etl脚本
     *
     * @return
     */
    public List<EtlDetailVo> EtlDatabaseList() {
        return technologyEtlTargetMapper.EtlDatabaseList();
    }
}
