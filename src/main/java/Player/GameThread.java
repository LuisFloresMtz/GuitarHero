
package Player;

public class GameThread extends Thread {
    private boolean paused = false;
    long currentTime;
    Tab tab;
    
    public GameThread(Tab tab) {
        this.tab = tab;
    }

    public boolean isPaused() {
        return paused;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public Tab getTab() {
        return tab;
    }
    
    @Override
    public void run() {
        long oldTime = System.currentTimeMillis();
            while (true) {
                if (!paused) {
                    //tab.draw();
                    
                    //System.out.println("Tiempo: " + currentTime);
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                currentTime = System.currentTimeMillis() - oldTime;
            }
    }
}
