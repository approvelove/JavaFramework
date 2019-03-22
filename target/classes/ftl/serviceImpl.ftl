package ${implPackageName};

import ${baseMapperImport};
import ${mapperImport};
import ${domainImport};
import ${abStractServiceImport};
import ${serviceImport};
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* Created by JamesLee on ${.now?string.short}
* Copyright © ${.now?string.yyyy} JamesLee. All rights reserved.
*/

@Transactional
@Service
public class ${domainName}ServiceImpl extends BaseServiceImpl<${domainName},${pkType}> implements ${domainName}Service {
    @Resource
    private ${domainName}Mapper ${sdomainName}Mapper;

    @Override
    public BaseMapper getBaseMapper() {
        return ${sdomainName}Mapper;
    }
}