package net.gobbob.mobends.client.model;

import org.lwjgl.opengl.GL11;

import net.gobbob.mobends.util.SmoothVector3f;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModelRendererBends extends ModelRenderer{
	
	public SmoothVector3f rotation = new SmoothVector3f();
	public SmoothVector3f pre_rotation = new SmoothVector3f();
	public float scaleX,scaleY,scaleZ;
	public int texOffsetX, texOffsetY;
	
	public boolean compiled;
	private int displayList;
	
	public boolean showChildIfHidden = false;
	
	public ModelRendererBends(ModelBase model, boolean register)
	{
		super(model);
		this.scaleX = this.scaleY = this.scaleZ = 1.0f;
		if(!register) model.boxList.remove(model.boxList.size() - 1);
	}
	
	public ModelRendererBends(ModelBase model, String name)
    {
		super(model, name);
		this.scaleX = this.scaleY = this.scaleZ = 1.0f;
    }
	
	public ModelRendererBends(ModelBase model, int texOffsetX, int texOffsetY)
    {
        super(model, texOffsetX, texOffsetY);
        this.texOffsetX = texOffsetX;
        this.texOffsetY = texOffsetY;
        this.scaleX = this.scaleY = this.scaleZ = 1.0f;
    }
	
	public void updateBends(float ticksPerFrame){
		this.rotateAngleX = this.rotation.getX() / 180.0f * (float)Math.PI;
		this.rotateAngleY = this.rotation.getY() / 180.0f * (float)Math.PI;
		this.rotateAngleZ = this.rotation.getZ() / 180.0f * (float)Math.PI;
	}
	
	public ModelRendererBends setShowChildIfHidden(boolean flag){
		this.showChildIfHidden = flag;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
    public void compileDisplayList(float p_78788_1_)
    {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, 4864);
        BufferBuilder vertexbuffer = Tessellator.getInstance().getBuffer();

        for (int i = 0; i < this.cubeList.size(); ++i)
        {
            ((ModelBox)this.cubeList.get(i)).render(vertexbuffer, p_78788_1_);
        }

        GlStateManager.glEndList();
        this.compiled = true;
    }
	
	@Override
	public void render(float p_78785_1_)
    {
		updateBends(p_78785_1_);

        if (!this.compiled)
        {
            this.compileDisplayList(p_78785_1_);
        }

        GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);

        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
        {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
            {
            	GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX,this.scaleY,this.scaleZ);
                
                if (!this.isHidden & this.showModel)
                GL11.glCallList(this.displayList);
                
                if((this.showChildIfHidden) || (!this.isHidden & this.showModel)){
                    if (this.childModels != null)
                    {
                        for (int i = 0; i < this.childModels.size(); ++i)
                        {
                            ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
                        }
                    }
                }
            }
            else
            {
                GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
                
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX,this.scaleY,this.scaleZ);
                
                if (!this.isHidden & this.showModel)
                GL11.glCallList(this.displayList);
                
                if((this.showChildIfHidden) || (!this.isHidden & this.showModel)){
                    if (this.childModels != null)
                    {
                        for (int i = 0; i < this.childModels.size(); ++i)
                        {
                            ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
                        }
                    }
                }

                GL11.glTranslatef(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
            }
        }
        else
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
            
            GL11.glRotatef(this.pre_rotation.getZ(), 0F, 0F, 1F);
            GL11.glRotatef(this.pre_rotation.getY(), 0F, 1F, 0F);
            GL11.glRotatef(this.pre_rotation.getX(), 1F, 0F, 0F);
            
            if (this.rotateAngleZ != 0.0F)
                GL11.glRotatef(this.rotation.getZ(), 0F, 0F, 1F);
            if (this.rotateAngleY != 0.0F)
                GL11.glRotatef(this.rotation.getY(), 0F, 1F, 0F);
            if (this.rotateAngleX != 0.0F)
                GL11.glRotatef(this.rotation.getX(), 1F, 0F, 0F);
            
            GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ);
            
            if (!this.isHidden & this.showModel)
            GL11.glCallList(this.displayList);
            
            if((this.showChildIfHidden) || (!this.isHidden & this.showModel)){
                if (this.childModels != null)
                {
                    for (int i = 0; i < this.childModels.size(); ++i)
                    {
                        ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
                    }
                }
            }

            GL11.glPopMatrix();
        }

        GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
    }
	
	public void update(float p_78785_1_){
		this.rotation.update(p_78785_1_);
		this.pre_rotation.update(p_78785_1_);
		updateBends(p_78785_1_);
	}
	
	@Override
    public void renderWithRotation(float p_78791_1_)
    {
		updateBends(p_78791_1_);
		super.renderWithRotation(p_78791_1_);
    }
    
	@Override
    public void postRender(float p_78794_1_)
    {
		updateBends(p_78794_1_);

        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
        {
            if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
            {
                GL11.glTranslatef(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
            
                GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
                GL11.glScalef(this.scaleX,this.scaleY,this.scaleZ);
            }
        }
        else
        {
            GL11.glTranslatef(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
            
            GL11.glRotatef(-this.pre_rotation.getY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(this.pre_rotation.getZ(), 0.0F, 0.0F, 1.0F);
            
            if (this.rotateAngleZ != 0.0F)
            {
                GL11.glRotatef(this.rotateAngleZ * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F)
            {
                GL11.glRotatef(this.rotateAngleY * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F)
            {
                GL11.glRotatef(this.rotateAngleX * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            }
            
            GL11.glScalef(this.scaleX,this.scaleY,this.scaleZ);
        }
    }
	
	public ModelRendererBends setPosition(float argX,float argY,float argZ){
		this.rotationPointX = argX;
		this.rotationPointY = argY;
		this.rotationPointZ = argZ;
		return this;
	}
	
	public ModelRendererBends setOffset(float argX, float argY, float argZ){
		this.offsetX = argX;
		this.offsetY = argY;
		this.offsetZ = argZ;
		return this;
	}
	
	public ModelRendererBends setScale(float argX, float argY, float argZ){
		this.scaleX = argX;
		this.scaleY = argY;
		this.scaleZ = argZ;
		return this;
	}
	
	public ModelRendererBends resetScale(){
		this.scaleX = this.scaleY = this.scaleZ = 1.0f;
		return this;
	}
	
	public void sync(ModelRendererBends box){
		if(box != null){
			this.rotateAngleX = box.rotateAngleX;
			this.rotateAngleY = box.rotateAngleY;
			this.rotateAngleZ = box.rotateAngleZ;
			this.rotation.vOld.set(box.rotation.vOld);
			this.rotation.completion.set(box.rotation.completion);
			this.rotation.vFinal.set(box.rotation.vFinal);
			this.rotation.vSmooth.set(box.rotation.vSmooth);
			this.rotation.smoothness.set(box.rotation.smoothness);

			this.pre_rotation.vOld.set(box.pre_rotation.vOld);
			this.pre_rotation.completion.set(box.pre_rotation.completion);
			this.pre_rotation.vFinal.set(box.pre_rotation.vFinal);
			this.pre_rotation.vSmooth.set(box.pre_rotation.vSmooth);
			this.pre_rotation.smoothness.set(box.pre_rotation.smoothness);
			
			this.scaleX = box.scaleX;
			this.scaleY = box.scaleY;
			this.scaleZ = box.scaleZ;
		}
	}
	
	@Override
	public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_, int p_78790_6_, float p_78790_7_)
    {
        this.cubeList.add(new ModelBoxBends(this, this.texOffsetX, this.texOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_));
        this.compiled = false;
    }
	
	public ModelBoxBends getBox(){
		return ((ModelBoxBends)this.cubeList.get(0));
	}
	
	public ModelRendererBends offsetBox(float x, float y, float z)
	{
		this.getBox().offset(x, y, z);
		return this;
	}
	
	public ModelRendererBends offsetBox_Add(float x, float y, float z)
	{
		this.getBox().offset_add(x, y, z);
		return this;
	}
	
	public ModelRendererBends resizeBox(float x, float y, float z)
	{
		this.getBox().resize(x, y, z);
		return this;
	}
	
	public ModelRendererBends updateVertices()
	{
		this.getBox().updateVertexPositions(this);
		this.compiled = false;
		return this;
	}
	
	public ModelRendererBends setTextureOffset(int x, int y)
    {
        this.texOffsetX = x;
        this.texOffsetY = y;
        return this;
    }
	
	public void finish() {
		this.rotation.finish();
		this.pre_rotation.finish();
	}
}