GomokuPosition pos = (GomokuPosition) p;
        short b;
        if (player) {
            b = GomokuPosition.HUMAN;
        } else {
            b = GomokuPosition.PROGRAM;
        }
        int count[][] = new int[number - 1][3];
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[0].length; j++) {
                count[i][j] = 0;
            }
        }

        for (int n = 2; n <= number; n++) {
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    if (i + n - 1 < 19) {
                        if (downSame(p, i, j, n - 1, b)) {
                            while (true) {
                                if (i + n < 19) {
                                    if (pos.grid[i + n][j] == b) {
                                        break;
                                    }
                                }
                                if (i - 1 >= 0) {
                                    if (pos.grid[i - 1][j] == b) {
                                        break;
                                    }
                                }

                                count[n - 2][0]++;

                                if (i - 1 >= 0 && i + n >= pos.grid.length) {
                                    if (pos.grid[i - 1][j] == GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }
                                } else if (i - 1 < 0 && i + n < pos.grid.length) {
                                    if (pos.grid[i + n][j] == GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }
                                } else if (i - 1 >= 0 && i + n < pos.grid.length) {
                                    if (pos.grid[i - 1][j] == GomokuPosition.BLANK
                                            && pos.grid[i + n][j] != GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }
                                    if (pos.grid[i + n][j] == GomokuPosition.BLANK
                                            && pos.grid[i - 1][j] != GomokuPosition.BLANK) {
                                        count[n - 2][1]++;
                                    }

                                    if (pos.grid[i - 1][j] == GomokuPosition.BLANK
                                            && pos.grid[i + n][j] == GomokuPosition.BLANK) {
                                        count[n - 2][2]++;
                                    }
                                }
                                break;
                            }
                        }

                        if (j + n - 1 < pos.grid[0].length) {
                            if (rightSame(p, i, j, n - 1, b)) {
                                while (true) {
                                    if (j + n < pos.grid[0].length) {
                                        if (pos.grid[i][j + n] == b) {
                                            break;
                                        }
                                    }
                                    if (j - 1 >= 0) {
                                        if (pos.grid[i][j - 1] == b) {
                                            break;
                                        }
                                    }
                                    count[n - 2][0]++;

                                    if (j - 1 >= 0 && j + n >= pos.grid[0].length) {
                                        if (pos.grid[i][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if (j - 1 < 0 && j + n < pos.grid[0].length) {
                                        if (pos.grid[i][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if (j - 1 >= 0 && j + n < pos.grid[0].length) {
                                        if (pos.grid[i][j - 1] == GomokuPosition.BLANK
                                                && pos.grid[i][j + n] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.grid[i][j + n] == GomokuPosition.BLANK
                                                && pos.grid[i][j - 1] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }

                                        if (pos.grid[i][j - 1] == GomokuPosition.BLANK
                                                && pos.grid[i][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][2]++;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (i + n - 1 < pos.grid.length && j + n - 1 < pos.grid[0].length) {
                            if (rightDownSame(p, i, j, n - 1, b)) {
                                while (true) {
                                    if (i + n < pos.grid.length && j + n < pos.grid[0].length) {
                                        if (pos.grid[i + n][j + n] == b) {
                                            break;
                                        }
                                    }
                                    if (i - 1 >= 0 && j - 1 >= 0) {
                                        if (pos.grid[i - 1][j - 1] == b) {
                                            break;
                                        }
                                    }
                                    count[n - 2][0]++;

                                    if ((i - 1 >= 0 && j - 1 >= 0) && (i + n >= pos.grid.length || j + n >= pos.grid[0].length)) {
                                        if (pos.grid[i - 1][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - 1 < 0 || j - 1 < 0) && (i + n < pos.grid.length && j + n < pos.grid[0].length)) {
                                        if (pos.grid[i + n][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - 1 >= 0 && j - 1 >= 0) && (i + n < pos.grid.length && j + n < pos.grid[0].length)) {
                                        if (pos.grid[i - 1][j - 1] == GomokuPosition.BLANK
                                                && pos.grid[i + n][j + n] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.grid[i + n][j + n] == GomokuPosition.BLANK
                                                && pos.grid[i - 1][j - 1] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.grid[i - 1][j - 1] == GomokuPosition.BLANK && pos.grid[i + n][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][2]++;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (i - n + 1 >= 0 && j + n - 1 < pos.grid[0].length) {
                            if (rightUpSame(p, i, j, n - 1, b)) {
                                while (true) {
                                    if (i - n >= 0 && j + n < pos.grid[0].length) {
                                        if (pos.grid[i - n][j + n] == b) {
                                            break;
                                        }
                                    }
                                    if (i + 1 < pos.grid.length && j - 1 >= 0) {
                                        if (pos.grid[i + 1][j - 1] == b) {
                                            break;
                                        }
                                    }
                                    count[n - 2][0]++;

                                    if ((i - n >= 0 && j + n < pos.grid[0].length) && (i + 1 >= pos.grid.length || j - 1 < 0)) {
                                        if (pos.grid[i - n][j + n] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - n < 0 || j + n > pos.grid[0].length) && (i + 1 < pos.grid.length && j - 1 >= 0)) {
                                        if (pos.grid[i + 1][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                    } else if ((i - n >= 0 && j + n < pos.grid[0].length) && (i + 1 < pos.grid.length && j - 1 >= 0)) {
                                        if (pos.grid[i - n][j + n] == GomokuPosition.BLANK
                                                && pos.grid[i + 1][j - 1] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.grid[i + 1][j - 1] == GomokuPosition.BLANK
                                                && pos.grid[i - n][j + n] != GomokuPosition.BLANK) {
                                            count[n - 2][1]++;
                                        }
                                        if (pos.grid[i - n][j + n] == GomokuPosition.BLANK && pos.grid[i + 1][j - 1] == GomokuPosition.BLANK) {
                                            count[n - 2][2]++;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return count;