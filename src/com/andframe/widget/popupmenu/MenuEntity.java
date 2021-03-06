package com.andframe.widget.popupmenu;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class MenuEntity implements MenuItem{

	private int mId;
	private CharSequence mTitle;
	
	public MenuEntity(int id,CharSequence charSequence) {
		mId = id;
		mTitle = charSequence;
	}

	@Override
	public boolean collapseActionView() {
		return false;
	}

	@Override
	public boolean expandActionView() {
		return false;
	}

	@Override
	public ActionProvider getActionProvider() {
		return null;
	}

	@Override
	public View getActionView() {
		return null;
	}

	@Override
	public char getAlphabeticShortcut() {
		return 0;
	}

	@Override
	public int getGroupId() {
		return 0;
	}

	@Override
	public Drawable getIcon() {
		return null;
	}

	@Override
	public Intent getIntent() {
		return null;
	}

	@Override
	public int getItemId() {
		return mId;
	}

	@Override
	public ContextMenuInfo getMenuInfo() {
		return null;
	}

	@Override
	public char getNumericShortcut() {
		return 0;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public SubMenu getSubMenu() {
		return null;
	}

	@Override
	public CharSequence getTitle() {
		return mTitle;
	}

	@Override
	public CharSequence getTitleCondensed() {
		return null;
	}

	@Override
	public boolean hasSubMenu() {
		return false;
	}

	@Override
	public boolean isActionViewExpanded() {
		return false;
	}

	@Override
	public boolean isCheckable() {
		return false;
	}

	@Override
	public boolean isChecked() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public MenuItem setActionProvider(ActionProvider arg0) {
		return null;
	}

	@Override
	public MenuItem setActionView(View arg0) {
		return null;
	}

	@Override
	public MenuItem setActionView(int arg0) {
		return null;
	}

	@Override
	public MenuItem setAlphabeticShortcut(char arg0) {
		return null;
	}

	@Override
	public MenuItem setCheckable(boolean arg0) {
		return null;
	}

	@Override
	public MenuItem setChecked(boolean arg0) {
		return null;
	}

	@Override
	public MenuItem setEnabled(boolean arg0) {
		return null;
	}

	@Override
	public MenuItem setIcon(Drawable arg0) {
		return null;
	}

	@Override
	public MenuItem setIcon(int arg0) {
		return null;
	}

	@Override
	public MenuItem setIntent(Intent arg0) {
		return null;
	}

	@Override
	public MenuItem setNumericShortcut(char arg0) {
		return null;
	}

	@Override
	public MenuItem setOnActionExpandListener(OnActionExpandListener arg0) {
		return null;
	}

	@Override
	public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener arg0) {
		return null;
	}

	@Override
	public MenuItem setShortcut(char arg0, char arg1) {
		return null;
	}

	@Override
	public void setShowAsAction(int arg0) {
		
	}

	@Override
	public MenuItem setShowAsActionFlags(int arg0) {
		return null;
	}

	@Override
	public MenuItem setTitle(CharSequence arg0) {
		return null;
	}

	@Override
	public MenuItem setTitle(int arg0) {
		return null;
	}

	@Override
	public MenuItem setTitleCondensed(CharSequence arg0) {
		return null;
	}

	@Override
	public MenuItem setVisible(boolean arg0) {
		return null;
	}

}
