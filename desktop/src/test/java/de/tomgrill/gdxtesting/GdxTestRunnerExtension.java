package de.tomgrill.gdxtesting;
import com.badlogic.gdx.Gdx;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import static org.mockito.Mockito.mock;

public class GdxTestRunnerExtension implements TestInstancePostProcessor, ApplicationListener {

	private static final Namespace NAMESPACE = Namespace.create(GdxTestRunnerExtension.class);

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
		storeInContext(context).put(GdxTestRunnerExtension.class, this);
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		new HeadlessApplication(this, conf);
		Gdx.gl = mock(GL20.class);
	}

	@Override
	public void create() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void render() {

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
	}

	private Store storeInContext(ExtensionContext context) {
		return context.getStore(NAMESPACE);
	}
}
