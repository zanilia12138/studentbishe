package com.health.communicate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.communicate.entity.Material;
import com.health.communicate.mapper.MaterialMapper;
import com.health.communicate.service.MaterialService;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
}