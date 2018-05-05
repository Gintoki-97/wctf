package cn.gin.wctf.common.util;

import org.junit.Test;

import cn.gin.wctf.common.ansy.AnsyTaskManager;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithNoResult;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithResult;

public class AnsyTaskTest {
    
    @Test
    public void testAnsyTask() {
        final AnsyTaskManager manager = AnsyTaskManager.getAnsyTaskManager();
        if(manager.isOccupied()) {
            new Thread(new Runnable() {
                public void run() {
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask1");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask2");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask3");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask4");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask5");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask6");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask7");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask8");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask9");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread1 AnsyTask10");
                        }
                    });
                }
            }).start();
            
            new Thread(new Runnable() {
                public void run() {
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask1");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask2");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask3");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask4");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask5");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask5");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask6");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask7");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask8");
                        }
                    });
                    manager.submit(new AnsyTaskWithNoResult() {
                        @Override
                        public void execute() {
                            System.out.println("Thread2 AnsyTask9");
                        }
                    });
                }
            }).start();
        }
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAnsyTaskWithResult() {
        final AnsyTaskManager manager = AnsyTaskManager.getAnsyTaskManager();
        if(manager.isOccupied()) {
            try {
                System.out.println("Before work start ......");
                Thread.sleep(2000);
                System.out.println("Before work end ......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            manager.submit(new AnsyTaskWithResult<String>() {

                @Override
                public void callback(String res) {
                    System.out.println("Before by-line work post process start ......");
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Before by-line work post process end ......");
                }

                @Override
                public String execute() {
                    System.out.println("Before by-line work start ......");
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Before by-line work end ......");
                    return "run success";
                }
            });
            System.out.println("Before work returned ......");
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}