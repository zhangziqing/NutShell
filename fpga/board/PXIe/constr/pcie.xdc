create_clock -period 10.000 -name pcie_x86_refclk -waveform {0.000 5.000} [get_ports CLK_IN_D_clk_p]
set_property PACKAGE_PIN AB35 [get_ports {CLK_IN_D_clk_n[0]}]
set_property PACKAGE_PIN AB34 [get_ports {CLK_IN_D_clk_p[0]}]

set_property PACKAGE_PIN W41 [get_ports {pcie_mgt_rxp[0]}]
set_property PACKAGE_PIN W42 [get_ports {pcie_mgt_rxn[0]}]
set_property PACKAGE_PIN Y34 [get_ports {pcie_mgt_txp[0]}]
set_property PACKAGE_PIN Y35 [get_ports {pcie_mgt_txn[0]}]

create_pblock pblock_assert_u
add_cells_to_pblock [get_pblocks pblock_assert_u] [get_cells -quiet [list assert_u]]
resize_pblock [get_pblocks pblock_assert_u] -add {SLICE_X77Y480:SLICE_X104Y659}
resize_pblock [get_pblocks pblock_assert_u] -add {DSP48E2_X6Y192:DSP48E2_X7Y263}
resize_pblock [get_pblocks pblock_assert_u] -add {ILKNE4_X1Y1:ILKNE4_X1Y1}
resize_pblock [get_pblocks pblock_assert_u] -add {IOB_X1Y468:IOB_X1Y557}
resize_pblock [get_pblocks pblock_assert_u] -add {RAMB18_X6Y192:RAMB18_X7Y263}
resize_pblock [get_pblocks pblock_assert_u] -add {RAMB36_X6Y96:RAMB36_X7Y131}
set_property SNAPPING_MODE ON [get_pblocks pblock_assert_u]
set_property C_CLK_INPUT_FREQ_HZ 300000000 [get_debug_cores dbg_hub]
set_property C_ENABLE_CLK_DIVIDER false [get_debug_cores dbg_hub]
set_property C_USER_SCAN_CHAIN 1 [get_debug_cores dbg_hub]
connect_debug_port dbg_hub/clk [get_nets assert_clk]
