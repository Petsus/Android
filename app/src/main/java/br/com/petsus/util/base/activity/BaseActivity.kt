package br.com.petsus.util.base.activity

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    open fun showLoading() {}

    open fun dismissLoading() {}

}