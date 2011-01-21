package com.googlecode.reaxion.game.overlay;

import com.googlecode.reaxion.game.model.character.Character;
import com.googlecode.reaxion.game.networking.ClientData;
import com.googlecode.reaxion.game.networking.HudInfoContainer;
import com.googlecode.reaxion.game.networking.NetworkingObjects;
import com.googlecode.reaxion.game.state.StageGameState;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;

public class ClientHudOverlay extends HudOverlay {

	public ClientHudOverlay() {
		super();
	}

	@Override
	public void update(StageGameState q) {
		ClientData cd = NetworkingObjects.cd;

		if(cd.getPlayerAttacks() != null) {
			passCharacterInfo(cd.getPlayerAttacks(), (int) (cd.getGaugecap()));
			HudInfoContainer play = cd.getPlayer(), part = cd.getPartner(), targ = cd.getTarget();

			// update attacks
			for (int i = 0; i < attacks.length; i++) {
				if (attacks[i] != null && cd.getCurrentAttack() != null
						&& attacks[i].isInstance(cd.getCurrentAttack())) {
					attackFill[i].setLocalTranslation(new Vector3f(98, 100 - 20 * i + 10, 0));
					attackFill[i].setSolidColor(gaugeCosts[i] >= gaugeCap ? attackUsed[1]
							: attackUsed[0]);
					attackBar[i].setLocalTranslation(new Vector3f(98, 100 - 20 * i + 10, 0));
					attackText[i].setLocalTranslation(new Vector3f(22 + 4, 100 - 20 * i + 18, 0));
					gaugeCostText[i].setLocalTranslation(new Vector3f(
							22 + attackFill[i].getWidth() - 10, 100 - 20 * i + 18, 0));
				} else {
					attackFill[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20 * i + 10, 0));
					if (gaugeCosts[i] != -1 && cd.getGauge() >= gaugeCosts[i]) {
						ColorRGBA[] temp1 = zPressed ? zPressedColors : gaugeColors;
						ColorRGBA[] temp2 = zPressed ? gaugeColors : zPressedColors;
						if (i <= 2)
							attackFill[i].setSolidColor(gaugeCosts[i] >= gaugeCap ? temp1[1]
									: temp1[0]);
						else
							attackFill[i].setSolidColor(gaugeCosts[i] >= gaugeCap ? temp2[1]
									: temp2[0]);
					} else
						attackFill[i].setSolidColor(attackUnavailable);
					attackBar[i].setLocalTranslation(new Vector3f(-22 + 98, 100 - 20 * i + 10, 0));
					attackText[i].setLocalTranslation(new Vector3f(4, 100 - 20 * i + 18, 0));
					gaugeCostText[i].setLocalTranslation(new Vector3f(
							attackFill[i].getWidth() - 10, 100 - 20 * i + 18, 0));
				}
			}

			// update opHealth
			float percentOpHp = 0;
			if (targ.maxHp != 0)
				percentOpHp = (float) Math.max(targ.hp / targ.maxHp, 0);
			opHealthFill.setLocalScale(new Vector3f(percentOpHp, 1, 1));
			opHealthFill.setLocalTranslation(new Vector3f(6 + 9 + 283 - (1 - percentOpHp) * 283,
					576 + 5, 0));
			opHealthFill.setSolidColor(new ColorRGBA((percentOpHp < .5) ? 1 : 0,
					(percentOpHp >= .25) ? 1 : 0, 0, 1));

			// update opName
			opName.setText(targ.name);
			opName.update();

			// update opHealthText
			opHealthText.setText((int) Math.max(targ.hp, 0) + "/" + targ.maxHp);
			opHealthText.update();

			// update ptHealth
			float percentPtHp = (float) Math.max(part.hp / part.maxHp, 0);
			ptHealthFill.setLocalScale(new Vector3f(percentPtHp, 1, 1));
			ptHealthFill.setLocalTranslation(new Vector3f(208 + 9 + 90 + (1 - percentPtHp) * 90,
					50 + 5, 0));
			ptHealthFill.setSolidColor(new ColorRGBA((percentPtHp < .5) ? 1 : 0,
					(percentPtHp >= .25) ? 1 : 0, 0, 1));

			// update ptHealthText
			ptHealthText.setText((int) Math.max(part.hp, 0) + "/" + (part.maxHp));
			ptHealthText.update();

			// update ptName
			ptName.setText(part.name);
			ptName.update();

			// update health
			float percentHp = (float) Math.max((play.hp / play.maxHp), 0);
			healthFill.setLocalScale(new Vector3f(percentHp, 1, 1));
			healthFill.setLocalTranslation(new Vector3f(431 + 9 + 171 + (1 - percentHp) * 171,
					50 + 5, 0));
			healthFill.setSolidColor(new ColorRGBA((percentHp < .5) ? 1 : 0, (percentHp >= .25) ? 1
					: 0, 0, 1));

			// update healthText
			healthText.setText((int) Math.max(play.hp, 0) + "/" + (play.maxHp));
			healthText.update();

			// update name
			name.setText(play.name);
			name.update();

			// update gauge
			float lowerFraction = (float) (cd.getMinGauge() / cd.getGaugecap());
			float percentGauge = (float) (cd.getGauge() / cd.getGaugecap());
			gaugeHighFill.setLocalScale(new Vector3f(percentGauge, 1, 1));
			gaugeHighFill.setLocalTranslation(new Vector3f(
					208 + 9 + 283 - (1 - percentGauge) * 283, 34 + 5, 0));
			gaugeLowFill.setLocalScale(new Vector3f(Math.min(percentGauge, lowerFraction), 1, 1));
			gaugeLowFill.setLocalTranslation(new Vector3f(208 + 9 + 283
					- (1 - Math.min(percentGauge, lowerFraction)) * 283, 34 + 5, 0));

			// update gaugeCount
			gaugeCount.setText("" + (int) cd.getGauge());
			gaugeCount.update();
			gaugeCount.setLocalTranslation(new Vector3f(210 + 576 * percentGauge, 34, 0));
		}
	}
}
