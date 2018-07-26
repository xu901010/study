package cn.com.xyc.study.springcloud.licensingservice.repository;

import cn.com.xyc.study.springcloud.licensingservice.model.License;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository //告诉spring boot 这是一个JPA存储库类 和为它生成动态代理
public interface LicenseRepository extends CrudRepository<License, String> {

    public List<License> findByOrganizationId(String organizationId);

    public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
