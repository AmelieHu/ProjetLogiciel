close all

%3000 images d'entrainement
%500 d�tections d'images

%pourcentage de d�tection
y1 = [52.2, 62.800003, 55.4, 64.0, 62.199997, 65.4, 65.4, 64.200005, 68.4, 69.4, 68.2, 68.4, 69.2, 70.0, 71.4, 70.4, 70.6, 69.0, 70.8, 71.4, 72.2, 72.399994, 73.6, 73.4, 73.4, 75.2, 74.8, 74.2, 74.2, 77.4, 77.0, 77.600006, 75.8, 75.2, 75.0, 75.8, 75.6, 79.2, 77.600006, 77.600006, 76.8, 74.0, 78.6, 77.600006, 78.4, 80.6, 80.0, 75.0, 80.8, 79.799995, 79.799995, 79.6, 77.4, 80.0, 78.799995, 81.6, 79.0, 80.8, 82.6, 78.0, 78.799995, 81.4, 80.6, 80.0, 80.6, 78.799995, 81.4, 81.0, 78.6, 79.799995, 79.799995, 82.4, 82.6, 79.0, 80.6, 81.6, 81.0, 80.0, 81.0, 80.8, 79.6, 83.600006, 81.4, 84.0, 82.200005, 84.799995, 84.0, 83.200005, 82.200005, 81.4, 82.200005, 85.399994, 82.0, 83.600006, 84.4, 81.2, 84.2, 80.4, 85.2, 83.8, 81.6, 80.6, 84.6, 84.4, 84.4, 83.0, 82.4, 79.6, 81.4, 83.8, 82.8, 82.6, 84.4, 82.200005, 85.2, 81.4, 87.8, 81.4, 83.600006, 83.0, 84.0, 83.4, 80.8, 84.4, 84.2, 84.0, 82.0, 83.8, 82.8, 85.0, 82.0, 81.6, 84.2, 87.0, 83.4, 84.4, 82.8, 82.200005, 83.0, 83.600006, 83.4, 82.6, 87.2, 86.4, 83.4, 83.200005, 81.4, 84.799995, 87.4, 84.0, 84.2, 84.4, 84.0, 84.0, 82.0, 84.2, 85.0, 83.600006, 86.0, 84.799995, 82.0, 85.0, 84.6, 86.6, 85.399994, 82.4, 85.2, 85.0, 84.6, 84.4, 85.2, 86.0, 84.799995, 83.0, 85.0, 85.0, 84.6, 84.6, 84.0, 85.0, 84.4, 84.799995, 83.8, 86.4, 86.2, 87.8, 86.2, 84.6, 83.0, 87.4];

%Ch score
y2 = [118, 115, 113, 108, 105, 98, 97, 93, 92, 87, 85, 83, 81, 79, 75, 75, 72, 70, 69, 68, 66, 65, 64, 62, 60, 60, 59, 57, 57, 56, 55, 54, 54, 52, 51, 51, 50, 49, 49, 48, 47, 46, 45, 46, 45, 44, 44, 43, 43, 43, 42, 42, 41, 41, 40, 40, 40, 39, 38, 39, 37, 37, 37, 37, 36, 36, 36, 35, 35, 35, 34, 35, 34, 34, 33, 33, 32, 32, 32, 32, 32, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29, 29, 29, 29, 28, 28, 28, 28, 28, 28, 28, 27, 28, 27, 27, 26, 26, 26, 26, 26, 26, 25, 26, 25, 25, 25, 25, 25, 25, 24, 25, 25, 24, 24, 24, 24, 24, 23, 23, 23, 23, 23, 23, 23, 23, 22, 23, 22, 22, 22, 22, 22, 22, 22, 22, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 20, 20, 20, 20, 20, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19, 18, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18];

t = (10:1:199);

plot(t, y1)
xlabel("Nombre de cluster")
ylabel("Pourcentage de r�ussite de reconnaissance")
figure
plot(t, y2)
xlabel("Nombre de cluster")
ylabel("Score de Calinski-Harabasz")

%histogramme

%200 clusters
%Entrainement sur 5000 images
%D�tection de 500 images
%CH = 29

%Pourcentage r�ussite 85.7%
h = [93.548386, 98.039215, 76.59575, 91.228065, 66.66667, 83.78378, 97.91667, 90.38461, 76.47059, 78.723404];
t_h = (0:1:9);

figure
bar(t_h, h)
hold on
plot([-2 11], [85.7 85.7], '--')
xlim([-1 10])
ylabel("Pourcentage de d�tection par chiffre (moyenne 86%)")
xlabel("chiffres")
