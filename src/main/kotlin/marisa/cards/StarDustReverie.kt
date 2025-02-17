package marisa.cards

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster
import marisa.action.StarDustReverieAction
import marisa.patches.AbstractCardEnum

class StarDustReverie : CustomCard(
    ID,
    NAME,
    IMG_PATH,
    COST,
    DESCRIPTION,
    CardType.SKILL,
    AbstractCardEnum.MARISA_COLOR,
    CardRarity.RARE,
    CardTarget.SELF
) {
    init {
        exhaust = true
    }

    override fun use(p: AbstractPlayer, unused: AbstractMonster?) {
        addToBot(
            StarDustReverieAction(upgraded)
        )
    }

    override fun makeCopy(): AbstractCard = StarDustReverie()

    override fun upgrade() {
        if (upgraded) return
        upgradeName()
        rawDescription = DESCRIPTION_UPG
        initializeDescription()
    }

    companion object {
        const val ID = "StarDustReverie"
        const val IMG_PATH = "img/cards/StarDustReverie.png"
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        val NAME = cardStrings.NAME
        val DESCRIPTION = cardStrings.DESCRIPTION
        val DESCRIPTION_UPG = cardStrings.UPGRADE_DESCRIPTION
        private const val COST = 0
    }
}
