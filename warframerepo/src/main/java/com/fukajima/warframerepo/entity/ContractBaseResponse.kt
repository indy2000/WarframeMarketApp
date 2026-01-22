package com.fukajima.warframerepo.entity

class ContractBaseResponse {
    var apiVersion: String = ""
    var data: List<ContractWeapon> = listOf()
}
class ContractWeapon {
    var id: String? = null
    var slug: String? = null
    var gameRef: String? = null
    var reqMasteryRank: Int? = null
    var i18n: ContractI18n? = null
    var weaponName: String? = null
    var group: String? = null
    var rivenType: String? = null
    var disposition: Double? = null

    fun getName():String?{
        return i18n?.en?.name
    }
}

class ContractI18n {
    var en: ContractLanguageContent? = null
}

class ContractLanguageContent{
    var name: String? = null
    var wikiLink: String? = null
    var icon: String? = null
    var thumb: String? = null
}