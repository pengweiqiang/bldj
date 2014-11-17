package com.bldj.lexiang.utils;

import android.graphics.Color;

public class R {
	public static final class string {

	}

	public static final class integer {
		public static final int TITLEBAR_HEIGHT = 72;
		public static final int APP_PADDING = 10;

        public static final int FUNCBAR_HEIGHT = 64;
        public static final int titlebar_icon_width = 42;
        public static final int titlebar_num_left_margin = 38;

        public static final int setting_item_height = 84;
        public static final int setting_section_height = 64;
	}

	public static final class color {
		public static final int default_bg = Color.parseColor("#F5F6F7");
		public static final int TEXT_GRAY = Color.parseColor("#828282");
		public static final int text_dark_gray = Color.parseColor("#262626");
        public static final int selector_pressed = Color.parseColor("#F3F9FF");
        
        public static final int popwindow_item_pressed = Color.parseColor("#EAEAEA");
        
        public static final int titlebar_icon_selector_pressed = Color.parseColor("#0041a3");
        public static final int bg_shadow_horizontal = Color.parseColor("#F1F3F4");

        public static final int bg_btn_unable = Color.parseColor("#0667D9");
        
        

        public static final int bg_tabbar = Color.parseColor("#F9F9F9");
        public static final int bg_divider = Color.parseColor("#E5E5E5");
        public static final int bg_download_panel = Color.parseColor("#5b5b5b");
        public static final int bg_titlebar = Color.parseColor("#ee5910");
        public static final int bg_setting_item_section = Color.parseColor("#F9F9F9");
        public static final int textcolor_setting_item_section = Color.parseColor("#616161");

    }

	public static final class drawable {
		private static final String path = "app_store/";
		public static final String pull_arrow_down = path
				+ "pull_arrow_down.png";
		public static final String loading = path
				+ "loading.png";
		public static final String icon_back = path
				+ "icon_back.png";
		
		public static final String custom_tab_indicator_normal = path
				+ "custom_tab_indicator_normal.9.png";
		public static final String custom_tab_indicator_selected = path
				+ "custom_tab_indicator_selected.9.png";
		
		public static final String default_bg_titlebar = path
				+ "default_bg_titlebar.png";
		public static final String bg_titlebar_btn_normal = path
				+ "bg_titlebar_btn_normal.9.png";
		public static final String bg_titlebar_btn_pressed = path
				+ "bg_titlebar_btn_pressed.9.png";
		public static final String bg_item_list = path + "bg_item_list.9.png";
		public static final String bg_item_list_pressed = path
				+ "bg_item_list_pressed.9.png";
		public static final String ratingbar_empty = path
				+ "ratingbar_empty.png";
		public static final String ratingbar_filled = path
				+ "ratingbar_filled.png";
		public static final String btn_appstore_ranking_normal = path
				+ "btn_appstore_ranking_normal.9.png";
		public static final String btn_appstore_ranking_checked = path
				+ "btn_appstore_ranking_checked.9.png";
		public static final String btn_appstore_category_normal = path
				+ "btn_appstore_category_normal.9.png";
		public static final String btn_appstore_category_checked = path
				+ "btn_appstore_category_checked.9.png";

		public static final String bg_ranking_num = path
				+ "bg_ranking_num.9.png";
		public static final String medal_gold = path + "medal_gold.png";
		public static final String medal_silver = path + "medal_silver.png";
		public static final String medal_bronze = path + "medal_bronze.png";

		public static final String icon_download = path + "icon_download.png";
		public static final String icon_update = path + "icon_update.png";
		public static final String icon_pause = path + "icon_pause.png";
		public static final String icon_downloading = path + "icon_downloading.png";
		public static final String icon_install = path + "icon_install.png";
		public static final String icon_play = path + "icon_play.png";
		public static final String icon_open = path + "icon_open.png";
		
		public static final String bg_mainpage_bottom = path + "bg_mainpage_bottom.png";
		public static final String pause_download_normal = path + "pause_download_normal.png";
		public static final String pause_download_pressed = path + "pause_download_pressed.png";
		
		public static final String waiting_download_normal = path + "waiting_download_normal.png";
		public static final String waiting_download_pressed = path + "waiting_download_pressed.png";
		
		public static final String failture_download_normal = path + "failture_download_normal.png";
		public static final String failture_download_pressed = path + "failture_download_pressed.png";
		
		public static final String cancle_download_normal = path + "cancle_download_normal.png";
		public static final String cancle_download_pressed = path + "cancle_download_pressed.png";
		
		public static final String continue_download_normal = path + "continue_download_normal.png";
		public static final String continue_download_pressed = path + "continue_download_pressed.png";
		
		public static final String btn_download_normal = path + "btn_download_normal.9.png";
		public static final String btn_download_pressed = path + "btn_download_pressed.9.png";
		
		public static final String icon_arrow_up = path + "icon_arrow_up.png";
		public static final String icon_arrow_down = path + "icon_arrow_down.png";
		
		public static final String bg_share_to_other = path + "bg_share_to_other.9.png";
		
		public static final String personal_center_change_account_normal = path + "personal_center_change_account_normal.9.png";
		public static final String personal_center_change_account_pressed = path + "personal_center_change_account_pressed.9.png";

        public static final String icon_office_normal = path + "icon_office_normal.9.png";
        public static final String icon_office_checked = path + "icon_office_checked.9.png";

        public static final String icon_marketing_normal = path + "icon_marketing_normal.9.png";
        public static final String icon_marketing_checked = path + "icon_marketing_checked.9.png";

        public static final String icon_app_normal = path + "icon_app_normal.9.png";
        public static final String icon_app_checked = path + "icon_app_checked.9.png";

        public static final String icon_news_normal = path + "icon_news_normal.9.png";
        public static final String icon_news_checked = path + "icon_news_checked.9.png";

        public static final String icon_setting_normal = path + "icon_setting_normal.9.png";
        public static final String icon_setting_checked = path + "icon_setting_checked.9.png";

        public static final String bg_tab_checked = path + "bg_tab_checked.png";
        public static final String bg_tab_checked_center = path + "bg_tab_checked_center.png";
        
        public static final String tab_hot_normal = path + "tab_hot_normal.png";
        public static final String tab_hot_selected = path + "tab_hot_selected.png";
        public static final String tab_new_normal = path + "tab_new_normal.png";
        public static final String tab_new_selected = path + "tab_new_selected.png";
        public static final String tab_favorite_normal = path + "tab_favorite_normal.png";
        public static final String tab_favorite_selected = path + "tab_favorite_selected.png";

        public static final String btn_titlebar_back = path + "btn_titlebar_back.png";
        public static final String btn_titlebar_edit = path + "btn_titlebar_edit.png";
        public static final String btn_titlebar_quit = path + "btn_titlebar_quit.png";
        public static final String btn_titlebar_shop = path + "btn_titlebar_shop.png";
        public static final String btn_titlebar_collect = path + "btn_titlebar_collect.png";
        public static final String btn_titlebar_collected = path + "btn_titlebar_collected.png";
        public static final String btn_titlebar_dump = path + "btn_titlebar_dump.png";
        public static final String btn_titlebar_search = path + "btn_titlebar_search.png";
        public static final String btn_titlebar_func = path + "btn_titlebar_func.png";
        
        public static final String icon_ppw_search = path + "icon_search.png";
        public static final String icon_ppw_order_list = path + "icon_order_list.png";
        public static final String icon_ppw_favorite_list = path + "icon_favorite_list.png";
        
        public static final String shadow_ppw = path + "shadow_ppw.png";

        public static final String btn_input_search = path + "btn_input_search.png";
        public static final String btn_input_close = path + "btn_input_close.png";

        public static final String icon_promotion = path + "icon_promotion.png";
        public static final String icon_hot = path + "icon_hot.png";

        public static final String close = path + "close.png";
        public static final String minus = path + "minus.png";
        public static final String plus = path + "plus.png";

        public static final String bg_sellpoint_green = path + "bg_sellpoint_green.png";
        public static final String bg_sellpoint_red = path + "bg_sellpoint_red.png";
        public static final String bg_sellpoint_blue = path + "bg_sellpoint_blue.png";

        public static final String flag_sellpoint_green = path + "flag_sellpoint_green.png";
        public static final String flag_sellpoint_red = path + "flag_sellpoint_red.png";
        public static final String flag_sellpoint_blue = path + "flag_sellpoint_blue.png";

        public static final String cb_normal = path + "cb_normal.png";
        public static final String cb_checked = path + "cb_checked.png";
        public static final String icon_arrow = path + "icon_arrow.png";

        public static final String bg_appstore_dot = path + "bg_appstore_dot.png";
        public static final String wave_dot = path + "wave_dot.png";
        public static final String icon_app_detail_arrow = path + "icon_app_detail_arrow.png";

        public static final String bg_shadow = path + "bg_shadow.png";
	}
}
