package marisa.cards

import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.monsters.AbstractMonster
import marisa.patches.AbstractCardEnum
import marisa.powers.Marisa.OrrerysSunPower

class OrrerysSun : CustomCard(
    ID,
    NAME,
    IMG_PATH,
    COST,
    DESCRIPTION,
    CardType.POWER,
    AbstractCardEnum.MARISA_COLOR,
    CardRarity.UNCOMMON,
    CardTarget.SELF
) {
    init {
        magicNumber = STACKS
        baseMagicNumber = magicNumber
    }

    override fun use(p: AbstractPlayer, unused: AbstractMonster?) {
        addToBot(
            ApplyPowerAction(
                p,
                p,
                OrrerysSunPower(p, magicNumber),
                magicNumber
            )
        )
    }

    override fun makeCopy(): AbstractCard = OrrerysSun()

    override fun upgrade() {
        if (upgraded) return
        upgradeName()
        upgradeMagicNumber(UPG_STC)
    }

    companion object {
        const val ID = "OrrerysSun"
        private val cardStrings = CardCrawlGame.languagePack.getCardStrings(ID)
        val NAME = cardStrings.NAME
        val DESCRIPTION = cardStrings.DESCRIPTION
        const val IMG_PATH = "img/cards/Orrey.png"
        private const val COST = 1
        private const val STACKS = 6
        private const val UPG_STC = 3
    }
}
