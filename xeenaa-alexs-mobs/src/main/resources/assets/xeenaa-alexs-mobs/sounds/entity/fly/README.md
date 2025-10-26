# Fly Sound Files

This directory requires 3 sound files in OGG Vorbis format for the Fly entity. The sound events are already registered in `ModSounds.java` and mapped in `sounds.json`, but actual audio files are missing.

## Required Files

### 1. `fly_ambient.ogg`
**Purpose:** Ambient buzzing sound played periodically when the fly is flying or landed
**Specifications:**
- Duration: 1-2 seconds (should be loopable)
- Format: OGG Vorbis
- Volume: Quiet (0.3-0.5) to avoid overwhelming players
- Pitch: High-pitched buzzing (1000-1500 Hz)
- Description: Subtle fly wing buzzing sound

**Recommended Sources:**
- Freesound.org: Search "fly buzz", "insect wings", "housefly"
- Example: https://freesound.org/search/?q=fly+buzz

### 2. `fly_hurt.ogg`
**Purpose:** Sound played when the fly takes damage (rare, since flies die in one hit)
**Specifications:**
- Duration: <1 second
- Format: OGG Vorbis
- Volume: Slightly louder (0.5-0.7)
- Pitch: Sharp, high-pitched squish or buzz
- Description: Quick distress sound

**Recommended Sources:**
- Freesound.org: Search "squish", "insect squash", "fly swat"
- Could use a shortened version of the ambient buzz at higher pitch

### 3. `fly_death.ogg`
**Purpose:** Sound played when the fly dies (health reaches 0)
**Specifications:**
- Duration: <1 second
- Format: OGG Vorbis
- Volume: Starts at 0.5, fades to silence
- Pitch: Buzz that drops and fades out (simulates wings slowing)
- Description: Fading buzz as fly falls

**Recommended Sources:**
- Freesound.org: Search "fly death", "insect buzz fade"
- Can be created by applying fade-out effect to ambient buzz

## Audio Conversion

If you have WAV or MP3 files, convert them to OGG Vorbis using:

### Using Audacity (Free):
1. Open the audio file in Audacity
2. File > Export > Export as OGG
3. Set quality to 5-7 (good balance of quality/size)
4. Save to this directory

### Using FFmpeg (Command Line):
```bash
ffmpeg -i input.wav -c:a libvorbis -q:a 5 output.ogg
```

## Testing

After adding the .ogg files:
1. Run `./gradlew build` to verify resource packing
2. Launch the game with `/serve_client`
3. Summon a fly: `/summon xeenaa-alexs-mobs:fly`
4. Listen for the ambient buzzing sound
5. Hit the fly to hear hurt/death sounds
6. Check logs for "missing sound" warnings (should be gone)

## Current Status

- [x] SoundEvent registry entries created (ModSounds.java)
- [x] sounds.json mappings added
- [x] Sound subtitles added to en_us.json
- [ ] fly_ambient.ogg (MISSING)
- [ ] fly_hurt.ogg (MISSING)
- [ ] fly_death.ogg (MISSING)

**Note:** The game will not crash due to missing sound files, but will log warnings and play no sound when events trigger.

## License Considerations

When sourcing sounds from Freesound.org:
- Check the license (CC0, CC-BY, CC-BY-NC)
- CC0: No attribution required (ideal for mods)
- CC-BY: Requires attribution (add to mod credits)
- Avoid proprietary or non-commercial licenses for public mod releases

## Future Improvements

Once placeholder sounds are added:
- Consider pitch variation in sounds.json (0.9-1.1 range)
- Add multiple sound variants (fly_ambient1.ogg, fly_ambient2.ogg)
- Adjust volume/attenuation based on playtesting feedback
