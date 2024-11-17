package com.example.adminsystem.service;

import com.example.adminsystem.entity.Catalogue;
import com.example.adminsystem.entity.ResMenu;

import java.util.List;

public interface CatalogueService {
    Catalogue getCatalogue(Integer id);
    List<Integer> getCatalogueAids();
    List<Catalogue> getCatalogueByAids(List<Integer> aid);
    List<Catalogue> getCatalogueList();
    String addCatalogue(Catalogue catalogue);
    void delCatalogue(Integer id);
    Boolean isPathExist(String path);
    Boolean isNameExist(String name);
    Boolean updateCatalogue(ResMenu resMenu);
}
