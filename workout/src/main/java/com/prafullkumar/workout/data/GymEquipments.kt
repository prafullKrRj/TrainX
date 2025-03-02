package com.prafullkumar.workout.data

import com.prafullkumar.workout.data.local.EquipmentEntity

data class GymEquipment(val id: Int, val name: String, val imageUrl: String) {
    fun toEquipmentEntity(): EquipmentEntity {
        return EquipmentEntity(id, name, imageUrl)
    }
}

val gymEquipment = listOf(
    GymEquipment(1, "Barbell", "https://example.com/images/barbell.jpg"),
    GymEquipment(2, "Dumbbell", "https://example.com/images/dumbbell.jpg"),
    GymEquipment(3, "Kettlebell", "https://example.com/images/kettlebell.jpg"),
    GymEquipment(4, "EZ Curl Bar", "https://example.com/images/ez_curl_bar.jpg"),
    GymEquipment(5, "Trap Bar", "https://example.com/images/trap_bar.jpg"),
    GymEquipment(6, "Adjustable Bench", "https://example.com/images/adjustable_bench.jpg"),
    GymEquipment(7, "Flat Bench", "https://example.com/images/flat_bench.jpg"),
    GymEquipment(8, "Squat Rack", "https://example.com/images/squat_rack.jpg"),
    GymEquipment(9, "Smith Machine", "https://example.com/images/smith_machine.jpg"),
    GymEquipment(10, "Power Rack", "https://example.com/images/power_rack.jpg"),
    GymEquipment(11, "Leg Press Machine", "https://example.com/images/leg_press_machine.jpg"),
    GymEquipment(12, "Hack Squat Machine", "https://example.com/images/hack_squat_machine.jpg"),
    GymEquipment(
        13,
        "Leg Extension Machine",
        "https://example.com/images/leg_extension_machine.jpg"
    ),
    GymEquipment(14, "Leg Curl Machine", "https://example.com/images/leg_curl_machine.jpg"),
    GymEquipment(15, "Calf Raise Machine", "https://example.com/images/calf_raise_machine.jpg"),
    GymEquipment(
        16,
        "Cable Crossover Machine",
        "https://example.com/images/cable_crossover_machine.jpg"
    ),
    GymEquipment(17, "Lat Pulldown Machine", "https://example.com/images/lat_pulldown_machine.jpg"),
    GymEquipment(18, "Seated Row Machine", "https://example.com/images/seated_row_machine.jpg"),
    GymEquipment(19, "Pec Deck Machine", "https://example.com/images/pec_deck_machine.jpg"),
    GymEquipment(20, "Chest Press Machine", "https://example.com/images/chest_press_machine.jpg"),
    GymEquipment(
        21,
        "Incline Chest Press Machine",
        "https://example.com/images/incline_chest_press_machine.jpg"
    ),
    GymEquipment(
        22,
        "Shoulder Press Machine",
        "https://example.com/images/shoulder_press_machine.jpg"
    ),
    GymEquipment(
        23,
        "Lateral Raise Machine",
        "https://example.com/images/lateral_raise_machine.jpg"
    ),
    GymEquipment(24, "Triceps Dip Machine", "https://example.com/images/triceps_dip_machine.jpg"),
    GymEquipment(25, "Biceps Curl Machine", "https://example.com/images/biceps_curl_machine.jpg"),
    GymEquipment(26, "Preacher Curl Bench", "https://example.com/images/preacher_curl_bench.jpg"),
    GymEquipment(
        27,
        "Triceps Pushdown Machine",
        "https://example.com/images/triceps_pushdown_machine.jpg"
    ),
    GymEquipment(28, "Treadmill", "https://example.com/images/treadmill.jpg"),
    GymEquipment(29, "Elliptical Trainer", "https://example.com/images/elliptical_trainer.jpg"),
    GymEquipment(30, "Stationary Bike", "https://example.com/images/stationary_bike.jpg"),
    GymEquipment(31, "Rowing Machine", "https://example.com/images/rowing_machine.jpg"),
    GymEquipment(32, "Stair Climber", "https://example.com/images/stair_climber.jpg"),
    GymEquipment(33, "Battle Ropes", "https://example.com/images/battle_ropes.jpg"),
    GymEquipment(34, "Medicine Ball", "https://example.com/images/medicine_ball.jpg"),
    GymEquipment(35, "Resistance Bands", "https://example.com/images/resistance_bands.jpg"),
    GymEquipment(36, "Weighted Vest", "https://example.com/images/weighted_vest.jpg"),
    GymEquipment(37, "Jump Rope", "https://example.com/images/jump_rope.jpg"),
    GymEquipment(38, "Sandbag", "https://example.com/images/sandbag.jpg"),
    GymEquipment(39, "Pull-Up Bar", "https://example.com/images/pull_up_bar.jpg"),
    GymEquipment(40, "Dip Station", "https://example.com/images/dip_station.jpg"),
    GymEquipment(41, "Ab Roller", "https://example.com/images/ab_roller.jpg"),
    GymEquipment(42, "Foam Roller", "https://example.com/images/foam_roller.jpg"),
    GymEquipment(43, "Smith Machine Row", "https://example.com/images/smith_machine_row.jpg"),
    GymEquipment(44, "Landmine Attachment", "https://example.com/images/landmine_attachment.jpg"),
    GymEquipment(45, "Sled Push Machine", "https://example.com/images/sled_push_machine.jpg"),
    GymEquipment(46, "Glute Ham Developer", "https://example.com/images/glute_ham_developer.jpg"),
    GymEquipment(47, "Hip Thrust Machine", "https://example.com/images/hip_thrust_machine.jpg"),
    GymEquipment(
        48,
        "Reverse Hyperextension Machine",
        "https://example.com/images/reverse_hyperextension.jpg"
    ),
    GymEquipment(49, "Yoke Carry Frame", "https://example.com/images/yoke_carry_frame.jpg"),
    GymEquipment(50, "Atlas Stones", "https://example.com/images/atlas_stones.jpg"),
    GymEquipment(51, "Sledgehammer", "https://example.com/images/sledgehammer.jpg"),
    GymEquipment(52, "Strongman Log", "https://example.com/images/strongman_log.jpg"),
    GymEquipment(53, "Indian Clubs", "https://example.com/images/indian_clubs.jpg"),
    GymEquipment(54, "Macebell", "https://example.com/images/macebell.jpg"),
    GymEquipment(55, "Parallettes", "https://example.com/images/parallettes.jpg")
)