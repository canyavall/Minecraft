import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Programmatic GeckoLib animation generator for Fly entity (no external dependencies).
 *
 * This tool generates .animation.json files programmatically without needing Blockbench.
 * Part of TASK-004 COMPARISON TEST (Approach A).
 *
 * @author implementation-agent
 * @version 1.0
 * @since 2025-10-26
 */
public class AnimationGenerator {

    /**
     * Main entry point - generates Fly animations.
     */
    public static void main(String[] args) {
        try {
            generateFlyAnimations();
            System.out.println("✅ Fly animations generated successfully!");
        } catch (IOException e) {
            System.err.println("❌ Failed to generate animations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates the Fly mob animations in GeckoLib format.
     *
     * Animations:
     * 1. idle - Gentle body bob (2s loop)
     * 2. fly - Wing flapping (1s loop, rapid)
     * 3. death - Tumble fall (1.5s, play once)
     */
    private static void generateFlyAnimations() throws IOException {
        String outputPath = "fly_programmatic.animation.json";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("{");
            writer.println("  \"format_version\": \"1.8.0\",");
            writer.println("  \"animations\": {");

            // IDLE ANIMATION
            writer.println("    \"animation.fly.idle\": {");
            writer.println("      \"loop\": true,");
            writer.println("      \"animation_length\": 2.0,");
            writer.println("      \"bones\": {");
            writer.println("        \"body\": {");
            writer.println("          \"position\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"1.0\": [0, 0.5, 0],");
            writer.println("            \"2.0\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"head\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [0, 10, 0],");
            writer.println("            \"1.0\": [0, 0, 0],");
            writer.println("            \"1.5\": [0, -10, 0],");
            writer.println("            \"2.0\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"wing_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"1.0\": [0, 0, 5],");
            writer.println("            \"2.0\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"wing_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"1.0\": [0, 0, -5],");
            writer.println("            \"2.0\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        }");
            writer.println("      }");
            writer.println("    },");

            // FLY ANIMATION
            writer.println("    \"animation.fly.fly\": {");
            writer.println("      \"loop\": true,");
            writer.println("      \"animation_length\": 0.5,");
            writer.println("      \"bones\": {");
            writer.println("        \"body\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.25\": [2.5, 0, 0],");
            writer.println("            \"0.5\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"wing_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.125\": [0, 0, 45],");
            writer.println("            \"0.25\": [0, 0, 0],");
            writer.println("            \"0.375\": [0, 0, -45],");
            writer.println("            \"0.5\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"wing_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.125\": [0, 0, -45],");
            writer.println("            \"0.25\": [0, 0, 0],");
            writer.println("            \"0.375\": [0, 0, 45],");
            writer.println("            \"0.5\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_front_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.25\": [10, 0, 0],");
            writer.println("            \"0.5\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_front_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.25\": [-10, 0, 0],");
            writer.println("            \"0.5\": [0, 0, 0]");
            writer.println("          }");
            writer.println("        }");
            writer.println("      }");
            writer.println("    },");

            // DEATH ANIMATION
            writer.println("    \"animation.fly.death\": {");
            writer.println("      \"loop\": false,");
            writer.println("      \"animation_length\": 1.5,");
            writer.println("      \"bones\": {");
            writer.println("        \"body\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [90, 0, 90],");
            writer.println("            \"1.0\": [180, 0, 180],");
            writer.println("            \"1.5\": [270, 0, 270]");
            writer.println("          },");
            writer.println("          \"position\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"1.5\": [0, -3, 0]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"wing_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.3\": [0, 0, 90],");
            writer.println("            \"1.5\": [0, 0, 90]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"wing_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.3\": [0, 0, -90],");
            writer.println("            \"1.5\": [0, 0, -90]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_front_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [45, 0, 45],");
            writer.println("            \"1.5\": [45, 0, 45]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_front_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [45, 0, -45],");
            writer.println("            \"1.5\": [45, 0, -45]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_middle_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [-45, 0, 45],");
            writer.println("            \"1.5\": [-45, 0, 45]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_middle_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [-45, 0, -45],");
            writer.println("            \"1.5\": [-45, 0, -45]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_back_left\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [90, 0, 45],");
            writer.println("            \"1.5\": [90, 0, 45]");
            writer.println("          }");
            writer.println("        },");
            writer.println("        \"leg_back_right\": {");
            writer.println("          \"rotation\": {");
            writer.println("            \"0.0\": [0, 0, 0],");
            writer.println("            \"0.5\": [90, 0, -45],");
            writer.println("            \"1.5\": [90, 0, -45]");
            writer.println("          }");
            writer.println("        }");
            writer.println("      }");
            writer.println("    }");

            writer.println("  }");
            writer.println("}");
        }

        System.out.println("Generated: " + outputPath);
        System.out.println("Animations: 3 (idle, fly, death)");
        System.out.println("Total duration: idle=2.0s, fly=0.5s, death=1.5s");
    }
}
