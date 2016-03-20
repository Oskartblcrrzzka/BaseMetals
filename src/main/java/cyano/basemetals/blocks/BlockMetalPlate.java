package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockMetalPlate extends net.minecraft.block.Block implements IOreDictionaryEntry, IMetalObject{


	/**
	 * Blockstate property
	 */
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
	final MetalMaterial metal;
	
	private static final float thickness = 1.0f / 16.0f;
	
	public BlockMetalPlate(MetalMaterial metal) {
		super(Material.iron);
        this.stepSound = SoundType.METAL;
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING,EnumFacing.NORTH));
		this.useNeighborBrightness = true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}


    @Override
    public boolean isFullCube(IBlockState bs) {
        return false;
    }
    
	@Override
	public IBlockState onBlockPlaced(final World w, final BlockPos coord, final EnumFacing face,
                                     final float partialX, final float partialY, final float partialZ,
                                     final int i, final EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, face);
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

    @Override
    public int getMetaFromState(final IBlockState bs) {
        int i = ((EnumFacing)bs.getValue(FACING)).getIndex();
        return i;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }
    
    
    @Override
    public AxisAlignedBB getBoundingBox(final IBlockState bs, final IBlockAccess world, final BlockPos coord) {
        final EnumFacing orientation = (EnumFacing) world.getBlockState(coord).getValue(FACING);
        float x1 = 0, x2 = 1, y1 = 0,y2 = 1, z1 = 0, z2 = 1;
        switch(orientation){
        case DOWN:
        	y1 = 1f - thickness;
        	break;
        case SOUTH:
        	z2 = thickness;
        	break;
        case NORTH:
        	z1 = 1f - thickness;
        	break;
        case EAST:
        	x2 = thickness;
        	break;
        case WEST:
        	x1 = 1f - thickness;
        	break;
        case UP:
        default:
        	y2 = thickness;
        	break;
        }
        // TODO: cache bounding box combos
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }
	@Override
    public void addCollisionBoxToList(final IBlockState bs, final World world, final BlockPos coord,
                                        final AxisAlignedBB box, final List<AxisAlignedBB> collisionBoxList,
                                        final Entity entity) {

        final EnumFacing orientation = (EnumFacing) world.getBlockState(coord).getValue(FACING);
        float x1 = 0, x2 = 1, y1 = 0,y2 = 1, z1 = 0, z2 = 1;
        switch(orientation){
        case DOWN:
        	y1 = 1f - thickness;
        	break;
        case SOUTH:
        	z2 = thickness;
        	break;
        case NORTH:
        	z1 = 1f - thickness;
        	break;
        case EAST:
        	x2 = thickness;
        	break;
        case WEST:
        	x1 = 1f - thickness;
        	break;
        case UP:
        default:
        	y2 = thickness;
        	break;
        }
        // TODO: replace with call to getBoundingBox() or with pre-calc cache
        AxisAlignedBB newbox = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
        super.addCollisionBoxToList(coord, box, collisionBoxList, newbox);
	}
	@Override
	public String getOreDictionaryName() {
		return "plate"+metal.getCapitalizedName();
	}

	@Override
	public MetalMaterial getMetalMaterial(){
		return metal;
	}
}
