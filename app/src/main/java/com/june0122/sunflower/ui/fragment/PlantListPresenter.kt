package com.june0122.sunflower.ui.fragment

interface PresenterView {

    fun success()

    fun fail()

    fun loadMoreSuccess(items: List<String>)
}

class PlantListPresenter(private val view: PresenterView) {

    fun load() {
        // 새로운 데이터를 로드
        view.success()

        //
        view.fail()
    }
}