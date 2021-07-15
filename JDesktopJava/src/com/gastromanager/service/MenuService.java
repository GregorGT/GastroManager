package com.gastromanager.service;

import com.gastromanager.models.MenuDetail;

public interface MenuService {

   MenuDetail loadMenu();
   void loadQuickMenuMap(MenuDetail menuDetail);
}
