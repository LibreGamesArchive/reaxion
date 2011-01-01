package com.googlecode.reaxion.game.audio;

public enum BackgroundMusic {

	CHALLENGE("challenge.ogg", "Challenge", "Final Fantasy X"), 
	THIRTEENTH_ANTHOLOGY("13th_anthology.ogg", "13th Anthology", "Kingdom Hearts 2"), 
	DEPRESSION("551 Depression (Legend of Hourai).ogg", "511 Depression", "Touhou Arrange"),
	FIRIING_PREPARATION("firing_preparation.ogg", "Firing Preparation", "Angel Beats!"),
	FORGOTTEN_CHALLENGE("forgotten_challenge.ogg", "Forgotten Challenge", "Kingdom Hearts Re:CoM"), 
	ATTACK("attack.ogg", "Attack", "Final Fantasy X Piano"),
	DJ_GOT_US_FALLIN_IN_LOVE("dj_got_us_fallin_in_love.ogg", "DJ Got Us Fallin' In Love", "Usher"), 
	DUST_TO_DUST("dust_to_dust.ogg", "Dust To Dust", "Final Fantasy XIII"), 
	GATEWAY_COLOSSEUM("gateway_colosseum.ogg", "Gateway Colosseum", "Pokemon Battle Revolution"), 
	JAPANIZE_DREAM("japanize_dream.ogg", "Japanize Dream", "Touhou"), 
	MITSU_NO_YOAKE("mitsu_no_yoake.ogg", "Mitsu No Yoake", "Spice and Wolf"), 
	NEBULA_GRAY("nebula_gray.ogg", "Nebula Gray", "Megaman Battle Netowrk"), 
	NIGHT_OF_FATE("night_of_fate.ogg", "Night of Fate", "Kingdom Hearts"), 
	NO_THANK_YOU("no_thank_you_mix2.ogg", "No Thank You", "K-ON!!"), 
	OLIVE("olive.ogg", "Olive", "Umbrella"), 
	PACKAGED("packaged.ogg", "Packaged", "Hatsune Miku"), 
	SAKURA_DISTORTION("sakura_distortion.ogg", "Sakura Distortion", "Touhou Arrange"),
	STAIRWAY_TO_SOLSTICE("stairway_to_solstice.ogg", "Stairway to Solstice", "Touhou Arrange"), 
	SUNLETH_WATERSCAPE("the_sunleth_waterscape.ogg", "The Sunleth Waterscape", "Final Fantasy XIII"), 
	TUMBLING("unversed_boss.ogg", "The Tumbling", "Kingdom Hearts: BbS"), 
	VIA_PURIFICO("via_purifico.ogg", "Via Purifico", "Final Fantasy X Piano"), 
	ZERO_TAIL("zero_tail.ogg", "Zero Tail", "Naruto Shippuuden Movie");
	
	private String filename;
	private String title;
	private String album;
	
	private BackgroundMusic(String filename, String title, String album) {
		this.filename = filename;
		this.title = title;
		this.album = album;
	}

	public String getFilename() {
		return filename;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAlbum() {
		return album;
	}
	
}
